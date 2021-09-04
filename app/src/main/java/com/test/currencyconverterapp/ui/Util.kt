package com.test.currencyconverterapp.ui

import java.text.NumberFormat
import java.util.*

object Util {
    fun toCurrency(amount: Double, currencyCode: String): String {
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 2
        format.currency = Currency.getInstance(currencyCode)
        return format.format(amount)
    }
}