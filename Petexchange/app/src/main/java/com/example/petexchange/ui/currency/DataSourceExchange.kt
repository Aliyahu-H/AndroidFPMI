package com.example.petexchange.ui.currency

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DataSourceExchange(resources: Resources) {
    private val initialFromCurrencyList = exchangeCurrencyList(resources)
    private val initialToCurrencyList = exchangeCurrencyList(resources)
    private val currenciesFromLiveData = MutableLiveData(initialFromCurrencyList)
    private val currenciesToLiveData = MutableLiveData(initialToCurrencyList)

    /* Adds currency to liveData and posts value. */
    fun changeFromCurrency(currency: Currency?) {
        val currentList = currenciesFromLiveData.value
        if (currentList == null) {
            currenciesFromLiveData.postValue(mutableSetOf(currency!!))
        } else {
            currentList.clear()
            currentList.add(currency!!)
            currenciesFromLiveData.postValue(currentList)
        }
    }

    /* Removes flower from liveData and posts value. */
    fun changeToCurrency(currency: Currency?) {
        val currentList = currenciesToLiveData.value
        if (currentList == null) {
            currenciesToLiveData.postValue(mutableSetOf(currency!!))
        } else {
            currentList.clear()
            currentList.add(currency!!)
            currenciesToLiveData.postValue(currentList)
        }
    }

    fun getFromCurrencyList(): LiveData<MutableSet<Currency>> {
        return currenciesFromLiveData
    }

    fun getToCurrencyList(): LiveData<MutableSet<Currency>> {
        return currenciesToLiveData
    }

    companion object {
        private var INSTANCE: DataSourceExchange? = null

        fun getDataSource(resources: Resources): DataSourceExchange {
            return synchronized(DataSourceExchange::class) {
                val newInstance = INSTANCE ?: DataSourceExchange(resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}