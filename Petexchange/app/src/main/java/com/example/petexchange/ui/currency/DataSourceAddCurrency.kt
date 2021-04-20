package com.example.petexchange.ui.currency

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DataSourceAddCurrency(resources: Resources) {
    private val initialCurrencyList = addCurrencyList(resources)
    private val currenciesLiveData = MutableLiveData(initialCurrencyList)

    /* Adds currency to liveData and posts value. */
    fun addCurrency(currency: Currency?) {
        val currentList = currenciesLiveData.value
        if (currentList == null) {
            currenciesLiveData.postValue(setOf(currency!!))
        } else {
            val updatedList = currentList.toMutableSet()
            updatedList.add(currency!!)
            currenciesLiveData.postValue(updatedList)
        }
    }

    /* Removes flower from liveData and posts value. */
    fun removeCurrency(currency: Currency) {
        val currentList = currenciesLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableSet()
            updatedList.remove(currency)
            currenciesLiveData.postValue(updatedList)
        }
    }

    fun getCurrencyList(): LiveData<Set<Currency>> {
        return currenciesLiveData
    }

    companion object {
        private var INSTANCE: DataSourceAddCurrency? = null

        fun getDataSource(resources: Resources): DataSourceAddCurrency {
            return synchronized(DataSourceAddCurrency::class) {
                val newInstance = INSTANCE ?: DataSourceAddCurrency(resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}