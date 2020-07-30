package com.cascade.intouch.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.cascade.intouch.R
import com.cascade.intouch.adapter.ItemClickListenerSummary
import com.cascade.intouch.adapter.SummaryAdapter
import com.cascade.intouch.model.Ticket
import kotlinx.android.synthetic.main.activity_summary.*

class SummaryActivity : AppCompatActivity(), SearchView.OnQueryTextListener, ItemClickListenerSummary,
        Observer<Any?> {

    private val liveDataSearchQuery = MutableLiveData<String?>()
    private lateinit var tickets: Array<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        // Initialize data
        val data = intent.extras!!.get(DATA) as Pair<*, *>
        tickets = data.second as Array<Ticket>
        title = data.first as String

        searchViewSummary.setOnQueryTextListener(this)
        processData(tickets as Array<Ticket>)
        swipeRefreshSummary.setOnRefreshListener {
            processData(tickets as Array<Ticket>)
        }

        liveDataSearchQuery.observe(this, this)
    }

    private fun processData(summaryData: Array<Ticket>) {
        summaryData.let {
            recyclerViewSummary.adapter = SummaryAdapter(it, this)
            recyclerViewSummary.smoothScrollToPosition(0)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return onQueryTextChange(query)
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        liveDataSearchQuery.postValue(newText)
        return true
    }

    override fun onItemClicked(itemView: View, data: Ticket) {
        startActivity(DetailActivity.newIntent(this, data))
    }

    override fun onChanged(t: Any?) {
        processData(tickets as Array<Ticket>)
    }

    companion object {
        private const val DATA = "data"

        fun newIntent(context: Context, data: Pair<String, Array<Ticket>>) =
                Intent(context, SummaryActivity::class.java).apply {
                    putExtra(DATA, data)
                }
    }
}