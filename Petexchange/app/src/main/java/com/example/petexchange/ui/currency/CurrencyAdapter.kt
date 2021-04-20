package com.example.petexchange.ui.currency

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.petexchange.R
import com.example.petexchange.databinding.CurrencyItemBinding


class CurrencyAdapter(private val currencies: LiveData<MutableSet<Currency>>, private val onClick: (Currency?) -> Unit) :
    RecyclerView.Adapter<CurrencyAdapter.CurrencyHolder>() {

    class CurrencyHolder(val binding: CurrencyItemBinding, val onClick: (Currency?) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                onClick(binding.currency)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CurrencyItemBinding.inflate(layoutInflater)

        return CurrencyHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: CurrencyHolder, position: Int) {
        val currentCurrency = currencies.value?.elementAt(position)
        holder.binding.currency = currentCurrency
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return currencies.value?.size!!
    }
}