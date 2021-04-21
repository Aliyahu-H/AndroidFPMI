package com.example.petexchange.ui.exchange

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.petexchange.ui.currency.Currency
import com.example.petexchange.ui.currency.DataSourceExchange
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ExchangeViewModel(val dataSource: DataSourceExchange) : ViewModel() {

    val currencyFromLiveData = dataSource.getFromCurrencyList()
    val currencyToLiveData = dataSource.getToCurrencyList()

    fun changeFromCurrency(currency: Currency?) {
        dataSource.changeFromCurrency(currency)
        dataSource.from = currency?.nameFrom
    }

    fun changeToCurrency(currency: Currency?) {
        dataSource.changeToCurrency(currency)
        dataSource.to = currency?.nameFrom
    }

    fun exchange() {
        GlobalScope.launch { dataSource.exchange() }
    }
}

class ExchangeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExchangeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExchangeViewModel(
                dataSource = DataSourceExchange.getDataSource(context.resources, context)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}