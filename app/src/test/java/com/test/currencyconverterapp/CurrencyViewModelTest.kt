package com.test.currencyconverterapp

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.test.currencyconverterapp.api.FakeCurrencyService
import com.test.currencyconverterapp.data.CurrencyDataSource
import com.test.currencyconverterapp.data.CurrencyRepository
import com.test.currencyconverterapp.model.CurrencyResponse
import com.test.currencyconverterapp.viewmodel.CurrencyViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyViewModelTest {

    @Mock
    lateinit var sharedPreferences: SharedPreferences

    @Mock
    lateinit var editor: SharedPreferences.Editor

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var currencyViewModel: CurrencyViewModel
    lateinit var currencyService: FakeCurrencyService
    lateinit var currencyDataSource: CurrencyDataSource
    lateinit var currencyRepository: CurrencyRepository

    fun fakeEmptyJson(): String {
        val quotes = hashMapOf<String, Double>()
        val currencyResponse = CurrencyResponse(true, "USD", quotes)
        return Gson().toJson(currencyResponse)
    }

    fun fakeJson(): String {
        val quotes = hashMapOf(
            "USDCAD" to 1.26025,
            "USDNGN" to 411.249809,
            "USDBIF" to 1980.416157
        )
        val currencyResponse = CurrencyResponse(true, "USD", quotes)
        return Gson().toJson(currencyResponse)
    }

    @Before
    @Throws(Exception::class)
    fun before() {
        Mockito.`when`(sharedPreferences.edit())
            .thenReturn(editor)
    }

    @Test
    fun getExchangeRateFromNetwork() = runBlocking {
        Mockito.`when`(sharedPreferences.getString(CurrencyDataSource.CACHE_KEY, null))
            .thenReturn(null)

        currencyService = FakeCurrencyService()
        currencyDataSource = CurrencyDataSource(currencyService, sharedPreferences)
        currencyRepository = CurrencyRepository(currencyDataSource)
        currencyViewModel = CurrencyViewModel(currencyRepository)

        currencyViewModel.getExchangeRate(ACCESS_TOKEN)
        Assert.assertEquals(
            currencyViewModel.mutableCurrency.value?.exchangeRates?.isEmpty(),
            false
        )
    }

    @Test
    fun getExchangeRateFromNetworkContainsValue() = runBlocking {
        Mockito.`when`(sharedPreferences.getString(CurrencyDataSource.CACHE_KEY, null))
            .thenReturn(null)

        currencyService = FakeCurrencyService()
        currencyDataSource = CurrencyDataSource(currencyService, sharedPreferences)
        currencyRepository = CurrencyRepository(currencyDataSource)
        currencyViewModel = CurrencyViewModel(currencyRepository)

        currencyViewModel.getExchangeRate(ACCESS_TOKEN)
        Assert.assertEquals(
            currencyViewModel.mutableCurrency.value?.exchangeRates?.contains(Pair("USDNGN", 411.249809)),
            true
        )
    }

    @Test
    fun getExchangeRateFromEmptyCache() = runBlocking {
        Mockito.`when`(sharedPreferences.getString(CurrencyDataSource.CACHE_KEY, null))
            .thenReturn(fakeEmptyJson())

        currencyService = FakeCurrencyService()
        currencyDataSource = CurrencyDataSource(currencyService, sharedPreferences)
        currencyRepository = CurrencyRepository(currencyDataSource)
        currencyViewModel = CurrencyViewModel(currencyRepository)

        currencyViewModel.getExchangeRate(ACCESS_TOKEN)
        Assert.assertEquals(
            currencyViewModel.mutableCurrency.value?.exchangeRates?.isEmpty(),
            true
        )
    }

    @Test
    fun getExchangeRateFromCache() = runBlocking {
        Mockito.`when`(sharedPreferences.getString(CurrencyDataSource.CACHE_KEY, null))
            .thenReturn(fakeJson())

        currencyService = FakeCurrencyService()
        currencyDataSource = CurrencyDataSource(currencyService, sharedPreferences)
        currencyRepository = CurrencyRepository(currencyDataSource)
        currencyViewModel = CurrencyViewModel(currencyRepository)

        currencyViewModel.getExchangeRate(ACCESS_TOKEN)
        Assert.assertEquals(
            currencyViewModel.mutableCurrency.value?.exchangeRates?.isEmpty(),
            false
        )
    }

    @Test
    fun getExchangeRateFromCacheContainsValue() = runBlocking {
        Mockito.`when`(sharedPreferences.getString(CurrencyDataSource.CACHE_KEY, null))
            .thenReturn(fakeJson())

        currencyService = FakeCurrencyService()
        currencyDataSource = CurrencyDataSource(currencyService, sharedPreferences)
        currencyRepository = CurrencyRepository(currencyDataSource)
        currencyViewModel = CurrencyViewModel(currencyRepository)

        currencyViewModel.getExchangeRate(ACCESS_TOKEN)
        Assert.assertEquals(
            currencyViewModel.mutableCurrency.value?.exchangeRates?.contains(Pair("USDCAD", 1.26025)),
            true
        )
    }

    @Test
    fun getExchangeRateForceLoadFromNetwork() = runBlocking {
        Mockito.`when`(sharedPreferences.getString(CurrencyDataSource.CACHE_KEY, null))
            .thenReturn(fakeEmptyJson())

        currencyService = FakeCurrencyService()
        currencyDataSource = CurrencyDataSource(currencyService, sharedPreferences)
        currencyRepository = CurrencyRepository(currencyDataSource)
        currencyViewModel = CurrencyViewModel(currencyRepository)

        currencyViewModel.getExchangeRate(ACCESS_TOKEN, true)
        Assert.assertEquals(
            currencyViewModel.mutableCurrency.value?.exchangeRates?.isEmpty(),
            false
        )
    }

    @Test
    fun getExchangeRateForceLoadFromNetworkContainsValue() = runBlocking {
        Mockito.`when`(sharedPreferences.getString(CurrencyDataSource.CACHE_KEY, null))
            .thenReturn(fakeEmptyJson())

        currencyService = FakeCurrencyService()
        currencyDataSource = CurrencyDataSource(currencyService, sharedPreferences)
        currencyRepository = CurrencyRepository(currencyDataSource)
        currencyViewModel = CurrencyViewModel(currencyRepository)

        currencyViewModel.getExchangeRate(ACCESS_TOKEN, true)
        Assert.assertEquals(
            currencyViewModel.mutableCurrency.value?.exchangeRates?.contains(Pair("USDCAD", 1.26025)),
            true
        )
    }

    companion object {
        const val ACCESS_TOKEN = "ACCESS TOKEN"
    }
}