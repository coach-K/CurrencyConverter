package com.test.currencyconverterapp.data

import com.test.currencyconverterapp.model.CurrencyResponse
import javax.inject.Inject

class CurrencyRepository @Inject constructor(private val dataSource: CurrencyDataSource) {
    suspend fun getCurrencyExchangeRate(
        accessToken: String,
        FORCE: Boolean = false
    ): CurrencyResponse {
        return dataSource.getCurrencyExchangeRage(accessToken, FORCE)
    }
}