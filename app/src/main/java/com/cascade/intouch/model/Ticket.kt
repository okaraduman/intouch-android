package com.cascade.intouch.model

import java.io.Serializable

data class Ticket(val originalMessage: String, val originalMessageUrl: String,
                  val summaryText: String, val outputMessage: String, val intents: List<String>) : Serializable