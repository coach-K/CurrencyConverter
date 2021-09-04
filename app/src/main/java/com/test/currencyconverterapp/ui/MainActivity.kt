package com.test.currencyconverterapp.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.currencyconverterapp.R
import com.test.currencyconverterapp.application.CurrencyApplication
import com.test.currencyconverterapp.data.CurrencyRepository
import com.test.currencyconverterapp.databinding.ActivityMainBinding
import com.test.currencyconverterapp.ui.adapter.CurrencyAdapter
import com.test.currencyconverterapp.viewmodel.CurrencyViewModel
import com.test.currencyconverterapp.viewmodel.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: CurrencyViewModel

    @Inject
    lateinit var repository: CurrencyRepository

    private var currencyAdapter: CurrencyAdapter? = null
    private var currencyCodeAdapter: ArrayAdapter<String>? = null
    private var currencyExchangeRates: List<Pair<String, Double>>? = null
    private var fromCurrency: Double? = null
    private var toCurrency: Double? = null
    private var toCurrencyCode: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as CurrencyApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelFactory(repository))
            .get(CurrencyViewModel::class.java)

        viewModel.mutableCurrency.observe(this, {
            currencyExchangeRates = it.exchangeRates
            val currencies = it.exchangeRates.map { pair ->
                pair.first.substring(3, 6)
            }
            if (currencyAdapter == null) {
                addCurrenciesToCurrencyAdapter(it.exchangeRates)
            } else {
                currencyAdapter?.addAll(it.exchangeRates)
            }
            if (currencyCodeAdapter == null) {
                addCurrenciesToCurrencyCodeAdapter(currencies)
            } else {
                currencyCodeAdapter?.clear()
                currencyCodeAdapter?.addAll(currencies)
                currencyCodeAdapter?.notifyDataSetChanged()
            }
        })

        viewModel.progressVisibility.observe(this, {
            binding.progressBar.visibility = it
        })

        binding.convertButton.setOnClickListener { performConvert() }

        fixedRateTimer("TIMED", false, THIRTY_MINUTES, THIRTY_MINUTES) {
            getExchangeRate(true)
        }

        getExchangeRate()
    }

    private fun addCurrenciesToCurrencyAdapter(exchangeRates: List<Pair<String, Double>>) {
        currencyAdapter = CurrencyAdapter(exchangeRates.toMutableList())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.recyclerView.adapter = currencyAdapter
    }

    private fun addCurrenciesToCurrencyCodeAdapter(currencies: List<String>) {
        currencyCodeAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, currencies)
        binding.fromCurrencySpinner.adapter = currencyCodeAdapter
        binding.toCurrencySpinner.adapter = currencyCodeAdapter
        binding.fromCurrencySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    currencyExchangeRates?.let {
                        val pair = it[position]
                        fromCurrency = (1.0 / pair.second)
                        changeSourceForExchangeRates(fromCurrency!!, currencies[position])
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

        binding.toCurrencySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    currencyExchangeRates?.let {
                        val pair = it[position]
                        toCurrency = (1.0 / pair.second)
                        toCurrencyCode = currencies[position]
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
    }

    fun changeSourceForExchangeRates(currency: Double, currencyCode: String) {
        val newCurrencyExchangeRates = currencyExchangeRates!!.map {
            val newCurrency = (1.0 / it.second)
            val newCurrencyCode = "$currencyCode${it.first.substring(3, 6)}"
            Pair(newCurrencyCode, (currency / newCurrency))
        }
        currencyAdapter?.addAll(newCurrencyExchangeRates)
    }

    private fun performConvert() {
        val amountString = binding.amountEditText.editableText.trim().toString()
        val amount = amountString.toDoubleOrNull()
        if (amount != null && fromCurrency != null && toCurrency != null) {
            val total = (fromCurrency!! / toCurrency!!) * amount
            binding.amountPreview.text = Util.toCurrency(total, toCurrencyCode)
        }
    }

    private fun getExchangeRate(FORCE: Boolean = false) {
        runBlocking {
            withContext(Dispatchers.IO) {
                viewModel.getExchangeRate(getString(R.string.access_token), FORCE)
            }
        }
    }

    companion object {
        const val THIRTY_MINUTES = 30L * 60L * 1000L
    }
}