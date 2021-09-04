package com.test.currencyconverterapp.dagger

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.test.currencyconverterapp.api.CurrencyService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule(val application: Application) {
    @Provides
    fun provideStorage(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application)
    }

    @Provides
    fun provideGsonConverter(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://api.currencylayer.com")
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    fun provideCurrencyService(retrofit: Retrofit): CurrencyService {
        return retrofit.create(CurrencyService::class.java)
    }
}