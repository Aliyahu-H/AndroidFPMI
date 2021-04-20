package com.example.petexchange.ui.addCurrency

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.petexchange.ui.currency.Currency
import com.example.petexchange.ui.currency.DataSourceAddCurrency

class AddCurrencyViewModel(val dataSource: DataSourceAddCurrency) : ViewModel() {

    val currencyLiveData = dataSource.getCurrencyList()

    fun insertCurrency(currency: Currency?) {
        dataSource.addCurrency(currency)
    }
}

class AddCurrencyViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddCurrencyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddCurrencyViewModel(
                    dataSource = DataSourceAddCurrency.getDataSource(context.resources)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}