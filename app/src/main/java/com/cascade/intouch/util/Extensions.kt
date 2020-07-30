package com.cascade.intouch.util

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.inflater(): LayoutInflater = LayoutInflater.from(this)