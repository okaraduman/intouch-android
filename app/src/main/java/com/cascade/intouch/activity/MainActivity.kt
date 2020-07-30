package com.cascade.intouch.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.cascade.intouch.R
import com.cascade.intouch.adapter.CategoryAdapter
import com.cascade.intouch.adapter.ItemClickListener
import com.cascade.intouch.data.Firm
import com.cascade.intouch.data.SortBy
import com.cascade.intouch.model.CategoryData
import com.cascade.intouch.model.Ticket
import com.cascade.intouch.network.RequestManager
import com.cascade.intouch.util.toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener, ItemClickListener,
    Observer<Any?> {
    private val liveDataSearchQuery = MutableLiveData<String?>()
    private val liveDataFirm = MutableLiveData<Firm?>()
    private val liveDataSortBy = MutableLiveData<SortBy>().apply {
        value = SortBy.DEFAULT
    }
    private var searchQuery: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipeRefresh.setOnRefreshListener {
            refreshData()
        }

        Firm.values().forEach {
            AppCompatButton(this).apply {
                tag = it
                text = it.name
            }.let {
                it.setOnClickListener { button ->
                    liveDataFirm.postValue(button.tag as Firm)
                    refreshData()
                    val inputManager: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(
                        currentFocus?.windowToken,
                        InputMethodManager.SHOW_FORCED
                    )
                }
                firmList.addView(it)
            }
        }

        liveDataSearchQuery.observe(this, this)
        liveDataSortBy.observe(this, this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return onQueryTextChange(query)
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        searchQuery = newText
        return true
    }

    override fun onChanged(data: Any?) {
        refreshData()
    }

    override fun onItemClicked(itemView: View, data: Pair<String, Array<Ticket>>) {
        showSummary(data)
    }

    private fun showSummary(data: Pair<String, Array<Ticket>>) {
        startActivity(SummaryActivity.newIntent(this, data))
    }

    private fun refreshData() {
        val environment = liveDataFirm.value
        val firm = searchFirmName.text.replace("\\s".toRegex(), "").toString()

        if (environment == null || "" == firm) {
            return
        }

        swipeRefresh.isRefreshing = true
        RequestManager.categoryData(environment.path, firm) { categoryData, throwable ->
            categoryData?.let {
                swipeRefresh.isRefreshing = false

                processData(it)
            } ?: run {
                swipeRefresh.isRefreshing = false
                recyclerViewCategory.adapter = null

                val errorMessage = throwable?.message ?: "error"
                toast(errorMessage)
            }
        }
    }

    private fun processData(categoryData: CategoryData) {
        categoryData.categoryMap?.let {
            val sortedData = categoryData.categoryMap.toList().let { category ->
                when (liveDataSortBy.value) {
                    SortBy.NAME_ASCENDING -> category.sortedBy { it.first }
                    SortBy.TICKET_SIZE -> category.sortedBy { it.second.size }
                    else -> category
                }
            }
            recyclerViewCategory.adapter = CategoryAdapter(sortedData, this)
            recyclerViewCategory.smoothScrollToPosition(0)
        } ?: run {
            recyclerViewCategory.adapter = null
        }
    }
}