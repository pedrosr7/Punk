package com.one2one.test.punk.core.extensions

import android.view.View
import android.widget.TextView

infix fun TextView.hideIfIsBlank (value: String?) {
    if (value != null) {
        if(value.isBlank()) this.visibility = View.GONE
    }
}