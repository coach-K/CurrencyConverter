package com.test.currencyconverterapp.api

import com.test.currencyconverterapp.model.CurrencyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyService {
    @GET("live")
    suspend fun liveExchangeRate(
        @Query("access_key") accessKey: String,
        @Query("format") format: String = "1"
    ): CurrencyResponse
}