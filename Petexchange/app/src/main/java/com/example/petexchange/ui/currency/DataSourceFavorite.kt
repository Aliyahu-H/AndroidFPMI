package com.example.petexchange.ui.currency

import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.petexchange.R
import com.example.petexchange.model.converters.Converter
import com.example.petexchange.model.converters.OneDayConverter
import com.example.petexchange.model.sources.database.exchangerate.room.RoomExchangeRateSaved
import com.example.petexchange.model.sources.exchangerate.exchangerate_api.receiver.RetrofitExchangeRateReceiver

class DataSourceFavorite(resources: Resources, context: Context) {
    private val converter : Converter = OneDayConverter(
        RetrofitExchangeRateReceiver(),
        RoomExchangeRateSaved(context))

    private var currenciesLiveData = MutableLiveData(mutableSetOf<Currency>())

    suspend fun onCreate() {
        currenciesLiveData.value?.add(Currency(R.drawable.ic_ruble, "RUB", "RUB", 1.0))
        converter.loadSavedExchangeRates().forEach{
            currenciesLiveData.value?.add(Currency(it))
        }
    }

    /* Adds currency to liveData and posts value. */
    suspend fun addCurrency(_currency: Currency?) {
        val currentList = currenciesLiveData.value
        val currency = Currency(
            _currency?.flag!!,
            _currency.nameTo!!,
            _currency.nameFrom!!,
            converter.getCurrencyRate(_currency.nameFrom!!, currentList?.elementAt(0)?.nameTo!!))
        if (!currentList.contains(currency)) {
            currentList.add(currency)

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
        private var INSTANCE: DataSourceFavorite? = null

        fun getDataSource(resources: Resources, context: Context): DataSourceFavorite {
            return synchronized(DataSourceFavorite::class) {
                val newInstance = INSTANCE ?: DataSourceFavorite(resources, context)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}