package com.test.currencyconverterapp.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.currencyconverterapp.data.CurrencyRepository
import com.test.currencyconverterapp.ui.CurrencyUiModel
import javax.inject.Inject

class CurrencyViewModel @Inject constructor(private val repository: CurrencyRepository) :
    ViewModel() {
    val mutableCurrency: MutableLiveData<CurrencyUiModel> = MutableLiveData()
    val progressVisibility: MutableLiveData<Int> = MutableLiveData()

    suspend fun getExchangeRate(
        accessToken: String,
        FORCE: Boolean = false
    ) {
        progressVisibility.postValue(View.VISIBLE)
        val response = repository.getCurrencyExchangeRate(accessToken, FORCE)
        val currencyUiModel = CurrencyUiModel(response.source, response.quotes.toList())
        mutableCurrency.postValue(currencyUiModel)
        progressVisibility.postValue(View.INVISIBLE)
    }
}