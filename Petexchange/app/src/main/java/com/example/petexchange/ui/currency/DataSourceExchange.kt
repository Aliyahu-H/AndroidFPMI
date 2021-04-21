package com.example.petexchange.ui.currency

import android.content.Context
import android.content.res.Resources
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.petexchange.model.converters.Converter
import com.example.petexchange.model.converters.OneDayConverter
import com.example.petexchange.model.sources.database.exchangerate.room.RoomExchangeRateSaved
import com.example.petexchange.model.sources.exchangerate.exchangerate_api.receiver.RetrofitExchangeRateReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.RuntimeException

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

    private suspend fun showAlertMessage(context: Context, e: RuntimeException) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Ошибка при конвертации:")
            .setMessage(e.message)
            .setPositiveButton("Ок") {
                    dialog, _ ->  dialog.cancel()
            }

        withContext(Dispatchers.Main) {builder.show()}
    }

    suspend fun exchange(context: Context) {
        val newToAmount: Double
        try {
            newToAmount = converter.convert(from!!, to!!, fromEcho.amount.get()?.toDouble()!!)
        } catch (e: RuntimeException) {
            showAlertMessage(context, e)
            return
        }

        toEcho.amount.set(newToAmount.toString())
    }

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