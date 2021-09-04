package com.test.currencyconverterapp.api

import com.test.currencyconverterapp.model.CurrencyResponse

class FakeCurrencyService : CurrencyService {
    override suspend fun liveExchangeRate(accessKey: String, format: String): CurrencyResponse {
        val quotes = hashMapOf(
            "USDCAD" to 1.26025,
            "USDNGN" to 411.249809,
            "USDBIF" to 1980.416157,
            "USDMWK" to 810.860122,
            "USDBYR" to 19600.0,
            "USDBYN" to 2.496063,
            "USDHUF" to 293.490006,
            "USDAOA" to 633.969032,
            "USDJPY" to 109.997044,
            "USDMNT" to 2850.901143
        )
        return CurrencyResponse(true, "USD", quotes)
    }
}