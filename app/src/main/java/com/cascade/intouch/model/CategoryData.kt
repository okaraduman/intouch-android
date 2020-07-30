package com.cascade.intouch.model

import com.google.gson.annotations.SerializedName

data class CategoryData(@SerializedName("categoryList") val categoryMap: HashMap<String, Array<Ticket>>?, val totalMessageCount: Int)