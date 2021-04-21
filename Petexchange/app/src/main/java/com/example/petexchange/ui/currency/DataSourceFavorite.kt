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
import java.lang.RuntimeException

class DataSourceFavorite(resources: Resources, context: Context) {
    private val converter : Converter = OneDayConverter(
        RetrofitExchangeRateReceiver(),
        RoomExchangeRateSaved(context))

    private var currenciesLiveData = MutableLiveData(mutableSetOf<Currency>())

    suspend fun onCreate() {
        currenciesLiveData.value?.clear()
        converter.loadSavedExchangeRates().forEach{
            currenciesLiveData.value?.add(Currency(it))
        }
        if (currenciesLiveData.value!!.isEmpty()) {
            currenciesLiveData.value?.add(Currency(
                R.drawable.ic_coin,
                "RUB",
                "RUB",
                converter.getCurrencyRate("RUB", "RUB")))
        }
    }

    fun showAlertMessage(e: RuntimeException) {
        
    }

    /* Adds currency to liveData and posts value. */
    suspend fun addCurrency(_currency: Currency?) {
        val currentList = currenciesLiveData.value
        val exchangeRate: Double
        try {
            exchangeRate = converter.getCurrencyRate(
                _currency?.nameFrom!!,
                currentList?.elementAt(0)?.nameTo!!
            )
        } catch (e: RuntimeException) {

            return
        }

        val currency = Currency(
            _currency.flag,
            currentList.elementAt(0).nameTo!!,
            _currency.nameFrom!!,
            exchangeRate)
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