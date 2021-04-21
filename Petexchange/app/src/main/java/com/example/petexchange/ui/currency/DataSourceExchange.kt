package com.example.petexchange.ui.currency

import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.petexchange.model.converters.Converter
import com.example.petexchange.model.converters.OneDayConverter
import com.example.petexchange.model.sources.database.exchangerate.room.RoomExchangeRateSaved
import com.example.petexchange.model.sources.exchangerate.exchangerate_api.receiver.RetrofitExchangeRateReceiver

class DataSourceExchange(resources: Resources, context: Context) {
    private val initialFromCurrencyList = exchangeCurrencyList(resources)
    private val initialToCurrencyList = exchangeCurrencyList(resources)
    private val currenciesFromLiveData = MutableLiveData(initialFromCurrencyList)
    private val currenciesToLiveData = MutableLiveData(initialToCurrencyList)
    var from: String? = currenciesFromLiveData.value?.elementAt(0)?.nameFrom
    var to: String? = currenciesToLiveData.value?.elementAt(0)?.nameFrom
    var fromEcho: Echo = Echo()
    var toEcho: Echo = Echo()

    private val converter : Converter = OneDayConverter(
            RetrofitExchangeRateReceiver(),
            RoomExchangeRateSaved(context))

    suspend fun exchange() {
        val newToAmount = converter.convert(from!!, to!!, fromEcho.amount.get()?.toDouble()!!)
        toEcho.amount.set(newToAmount.toString())
    }

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

        fun getDataSource(resources: Resources, context: Context): DataSourceExchange {
            return synchronized(DataSourceExchange::class) {
                val newInstance = INSTANCE ?: DataSourceExchange(resources, context)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}