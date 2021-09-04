package com.test.currencyconverterapp.data

import android.content.SharedPreferences
import com.google.gson.Gson
import com.test.currencyconverterapp.api.CurrencyService
import com.test.currencyconverterapp.model.CurrencyResponse
import javax.inject.Inject

class CurrencyDataSource @Inject constructor(
    private val currencyService: CurrencyService,
    private val storage: SharedPreferences
) {
    private var CACHE: String? = null

    suspend fun getCurrencyExchangeRage(
        accessToken: String,
        FORCE: Boolean = false
    ): CurrencyResponse {
        CACHE = fetchCache()
        if (CACHE == null || FORCE) {
            val currencyResponse = currencyService.liveExchangeRate(accessToken)
            CACHE = Gson().toJson(currencyResponse)
            storeCache(CACHE!!)
            return currencyResponse
        }
        return Gson().fromJson(CACHE, CurrencyResponse::class.java)
    }

    private fun storeCache(cacheValue: String) {
        val editor = storage.edit()
        editor.putString(CACHE_KEY, cacheValue)
        editor.apply()
    }

    private fun fetchCache(): String? {
        return storage.getString(CACHE_KEY, null)
    }

    companion object {
        const val CACHE_KEY = "CURRENCY_RESPONSE_KEY"
    }
}