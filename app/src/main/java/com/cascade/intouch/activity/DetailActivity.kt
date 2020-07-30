package com.cascade.intouch.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cascade.intouch.R
import com.cascade.intouch.model.Ticket
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.item_ticket.view.*

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Initialize data
        val data = intent.extras!!.get(DATA_TICKET) as Ticket
        title = "Details"
        addTicketHeader(detailCreator(data))

    }

    private fun detailCreator(data: Ticket): ArrayList<Pair<String, String>> {
        val detailList: ArrayList<Pair<String, String>> = ArrayList()
        detailList.add(Pair("Summary", data.summaryText))
        detailList.add(Pair("Original Message", data.originalMessage))
        detailList.add(Pair("Url", data.originalMessageUrl))
        detailList.add(Pair("Output", data.outputMessage))
        return detailList
    }

    private fun addTicketHeader(details: ArrayList<Pair<String, String>>) {
        for (detail in details) {
            LayoutInflater.from(this).inflate(R.layout.item_ticket, detailContent, false)
                    .apply {

                        detailLabel.text = detail.first
                        detailValue.text = detail.second
                        if (detail.first == "Summary" || detail.first == "Original Message") {
                            detailContent.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
                        }else {
                            detailContent.textAlignment = View.TEXT_ALIGNMENT_VIEW_END
                        }
                    }.let {
                        detailContent.addView(it)
                    }
        }
    }

    companion object {
        private const val DATA_TICKET = "dataTicket"

        fun newIntent(context: Context, data: Ticket) =
                Intent(context, DetailActivity::class.java).apply {
                    putExtra(DATA_TICKET, data)
                }
    }
}

