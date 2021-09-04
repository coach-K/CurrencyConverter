package com.test.currencyconverterapp.application

import android.app.Application
import com.test.currencyconverterapp.dagger.DaggerApplicationComponent
import com.test.currencyconverterapp.dagger.NetworkModule

class CurrencyApplication: Application() {
    val appComponent = DaggerApplicationComponent.builder()
        .networkModule(NetworkModule(this))
        .build()!!
}