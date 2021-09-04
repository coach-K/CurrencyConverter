package com.test.currencyconverterapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.currencyconverterapp.databinding.CurrencyItemViewBinding

class CurrencyAdapter(private val exchangeCurrencies: MutableList<Pair<String, Double>>) :
    RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding =
            CurrencyItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(exchangeCurrencies[position])
    }

    override fun getItemCount() = exchangeCurrencies.size

    fun addAll(_exchangeCurrencies: List<Pair<String, Double>>) {
        exchangeCurrencies.clear()
        exchangeCurrencies.addAll(_exchangeCurrencies)
        notifyDataSetChanged()
    }

    inner class CurrencyViewHolder(private val binding: CurrencyItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pair: Pair<String, Double>) {
            binding.exchangeCurrency.text = pair.first
            binding.exchangeAmount.text = pair.second.toString()
        }
    }
}