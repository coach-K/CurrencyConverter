package com.test.currencyconverterapp.model

data class CurrencyResponse(
    val success: Boolean,
    val source: String,
    val quotes: HashMap<String, Double>
) {
}