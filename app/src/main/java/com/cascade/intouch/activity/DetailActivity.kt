package com.cascade.intouch.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.cascade.intouch.R.drawable.speech_bubble_incoming
import com.cascade.intouch.R.drawable.speech_bubble_outcoming
import com.cascade.intouch.R.layout
import com.cascade.intouch.model.Ticket
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.item_ticket.view.*

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_detail)

        // Initialize data
        val data = intent.extras!!.get(DATA_TICKET) as Ticket
        title = "Details"
        addTicketHeader(detailCreator(data))
    }

    private fun detailCreator(data: Ticket): ArrayList<Pair<String, String>> {
        val detailList: ArrayList<Pair<String, String>> = ArrayList()
        detailList.add(Pair("Original Message", data.originalMessage))
        detailList.add(Pair("Output", data.outputMessage))
        detailList.add(Pair("Url", data.originalMessageUrl))
        return detailList
    }

    private fun addTicketHeader(details: ArrayList<Pair<String, String>>) {
        for (detail in details) {
            LayoutInflater.from(this).inflate(layout.item_ticket, detailContent, false)
                .apply {

                    detailValue.text = detail.second
                    if (detail.first == "Original Message") {
                        detailValue.setBackgroundResource(speech_bubble_outcoming)
                        detailValue.setPadding(30, 30, 45, 30)

                    } else {
                        detailValue.setBackgroundResource(speech_bubble_incoming)
                        detailValue.setPadding(50, 30, 30, 30)
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

