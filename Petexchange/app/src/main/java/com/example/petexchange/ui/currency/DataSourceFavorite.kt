package com.example.petexchange.ui.currency

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DataSourceFavorite(resources: Resources) {
    private val initialCurrencyList = favoriteCurrencyList(resources)
    private val currenciesLiveData = MutableLiveData(initialCurrencyList)

    /* Adds currency to liveData and posts value. */
    fun addCurrency(currency: Currency?) {
        val currentList = currenciesLiveData.value
        if (currentList == null) {
            currenciesLiveData.postValue(setOf(currency!!))
        } else {
            if (!currentList.contains(currency)) {
                val updatedList = currentList.toMutableSet()
                updatedList.add(currency!!)

                currenciesLiveData.postValue(updatedList)
            }
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
        private var INSTANCE: DataSourceFavorite? = null

        fun getDataSource(resources: Resources): DataSourceFavorite {
            return synchronized(DataSourceFavorite::class) {
                val newInstance = INSTANCE ?: DataSourceFavorite(resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}