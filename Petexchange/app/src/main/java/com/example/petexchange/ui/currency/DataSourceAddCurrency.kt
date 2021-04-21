package com.example.petexchange.ui.currency

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DataSourceAddCurrency(resources: Resources) {
    private val initialCurrencyList = addCurrencyList(resources)
    private val currenciesLiveData = MutableLiveData(initialCurrencyList)

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