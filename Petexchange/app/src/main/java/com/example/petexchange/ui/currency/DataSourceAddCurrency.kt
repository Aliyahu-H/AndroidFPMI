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
            currenciesLiveData.postValue(mutableSetOf(currency!!))
        } else {
            currentList.add(currency!!)
            currenciesLiveData.postValue(currentList)
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

    fun getCurrencyList(): LiveData<MutableSet<Currency>> {
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