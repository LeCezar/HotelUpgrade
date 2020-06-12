package com.lecezar.hotelupgrade.utils


import android.content.Context
import android.text.format.DateFormat
import java.util.*


fun Date.format_mm_DD_YY(context: Context): String {
    return DateFormat.getMediumDateFormat(context).format(this)
}

fun String.eliminateSpaces():String {
    this.filter {
        it != ' '
    }
    return this
}

fun Date.getDayAsInt(): Int = DateFormat.format("dd",   this).toString().toInt()

fun Date.getMonthAsInt(): Int = DateFormat.format("MM",   this).toString().toInt()

fun Date.getYearAsInt(): Int = DateFormat.format("yyyy",   this).toString().toInt()
