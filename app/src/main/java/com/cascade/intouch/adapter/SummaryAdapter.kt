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


interface ItemClickListenerSummary {
    fun onItemClicked(itemView: View, data: Ticket)
}

class SummaryAdapter(
        private val ticketList: Array<Ticket>,
        private val itemClickListener: ItemClickListenerSummary
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),Serializable {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = parent.context.inflater().inflate(R.layout.item_category, parent, false) as ViewGroup

        return SummaryViewHolder(view, itemClickListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(holder) {
            if (this is SummaryViewHolder) {
                clear()
                bind(ticketList[position])
            }
        }
    }

    override fun getItemCount() = ticketList.size
}

class SummaryViewHolder(
        override val containerView: ViewGroup,
        private val itemClickListener: ItemClickListenerSummary
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

    fun bind(ticket: Ticket) = with(containerView) {
        setOnClickListener {
            itemClickListener.onItemClicked(it, ticket)
        }

        textViewCategoryName.text = ticket.summaryText
    }
}
