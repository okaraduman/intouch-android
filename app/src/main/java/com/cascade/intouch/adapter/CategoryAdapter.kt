package com.cascade.intouch.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.cascade.intouch.R
import com.cascade.intouch.model.Ticket
import com.cascade.intouch.util.inflater
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_category.view.*
import java.io.Serializable

interface ItemClickListener {
    fun onItemClicked(itemView: View, data: Pair<String, Array<Ticket>>)
}

class CategoryAdapter(
        private val categoryList: List<Pair<String, Array<Ticket>>>,
        private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),Serializable {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = parent.context.inflater().inflate(R.layout.item_category, parent, false) as ViewGroup

        return CategoryViewHolder(view, itemClickListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(holder) {
            if (this is CategoryViewHolder) {
                clear()
                bind(categoryList[position])
            }
        }
    }

    override fun getItemCount() = categoryList.size
}

class CategoryViewHolder(
        override val containerView: ViewGroup,
        private val itemClickListener: ItemClickListener
) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun clear() = with(containerView) {
        children.filter {
            it is TextView
        }.forEach {
            it as TextView
            it.text = null
        }
        setOnClickListener(null)
    }

    fun bind(ticketData: Pair<String, Array<Ticket>>) = with(containerView) {
        setOnClickListener {
            itemClickListener.onItemClicked(it, ticketData)
        }

        textViewCategoryName.text = "${ticketData.first} - ${ticketData.second.size}"
    }
}