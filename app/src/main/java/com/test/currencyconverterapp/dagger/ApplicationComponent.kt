package com.test.currencyconverterapp.dagger

import com.test.currencyconverterapp.ui.MainActivity
import dagger.Component

@Component(modules = [NetworkModule::class])
interface ApplicationComponent {
    fun inject(activity: MainActivity)
}