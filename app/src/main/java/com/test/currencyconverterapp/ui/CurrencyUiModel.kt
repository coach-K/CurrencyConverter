package com.test.currencyconverterapp.ui

data class CurrencyUiModel(val currency: String, val exchangeRates: List<Pair<String, Double>>) {
}