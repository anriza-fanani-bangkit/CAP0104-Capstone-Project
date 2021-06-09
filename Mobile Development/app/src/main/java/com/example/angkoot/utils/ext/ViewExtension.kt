package com.example.angkoot.utils.ext

import android.view.View
import android.widget.EditText

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun EditText.text() = text.toString()