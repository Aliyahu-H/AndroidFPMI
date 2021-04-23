package com.example.petexchange.model.converters

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.petexchange.model.sources.database.CurrencyRate
import com.example.petexchange.model.sources.database.exchangerate.ExchangeRateSaved
import com.example.petexchange.model.sources.exchangerate.ExchangeRateReceiver
import org.junit.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.*

@RunWith(AndroidJUnit4::class)
class OneDayConverterTest {

    @Test
    fun testWithoutDatabaseRecordConvert() {
        val converter = OneDayConverter(dummyExchangeRateReceiver, dummyExchangeRateSaved)
        runBlocking {
            assertEquals(16.0, converter.convert("EUR", "BRL", 2.0), 0.001)
            assertEquals(2.5, converter.convert("EUR", "USD", 2.0), 0.001)
        }
    }

    @Test
    fun testWithNewDatabaseRecordConvert() {
        val converter = OneDayConverter(dummyExchangeRateReceiver, dummyExchangeRateSaved)
        runBlocking {
            assertEquals(2.5, converter.convert("EUR", "CUC", 2.0), 0.001)
        }
    }

    @Test
    fun testWithOldDatabaseRecordConvert() {
        val converter = OneDayConverter(dummyExchangeRateReceiver, dummyExchangeRateSaved)
        runBlocking {
            assertEquals(1.6, converter.convert("USD", "EUR", 2.0), 0.001)
        }
    }

    @Test
    fun testWithoutDatabaseRecordGetCurrencyRate() {
        val converter = OneDayConverter(dummyExchangeRateReceiver, dummyExchangeRateSaved)
        runBlocking {
            assertEquals(8.0, converter.getCurrencyRate("EUR", "BRL"), 0.001)
            assertEquals(1.25, converter.getCurrencyRate("EUR", "USD"), 0.001)
        }
    }

    @Test
    fun testWithNewDatabaseRecordGetCurrencyRate() {
        val converter = OneDayConverter(dummyExchangeRateReceiver, dummyExchangeRateSaved)
        runBlocking {
            assertEquals(1.25, converter.getCurrencyRate("EUR", "CUC"), 0.001)
        }
    }

    @Test
    fun testWithOldDatabaseRecordGetCurrencyRate() {
        val converter = OneDayConverter(dummyExchangeRateReceiver, dummyExchangeRateSaved)
        runBlocking {
            assertEquals(0.8, converter.getCurrencyRate("USD", "EUR"), 0.001)
        }
    }

    @Test
    fun testDeleteExchangeRates() {}

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd z", Locale.getDefault())

    private val dummyExchangeRateReceiver = object : ExchangeRateReceiver {
        private val newValues = listOf(CurrencyRate("RUB", "RUB", 1.0, ""),
                CurrencyRate("USD", "RUB", 50.0, ""),
                CurrencyRate("USD", "EUR", 0.8, ""),
                CurrencyRate("BRL", "EUR", 0.125, "")
        )

        override suspend fun getExchangeRates(fromCurrency: String): Map<String, Double> {
            val answer = HashMap<String, Double>()
            newValues.forEach {
                if (it.fromCurrency == fromCurrency) {
                    answer[it.toCurrency] = it.exchangeRate
                } else if (it.toCurrency == fromCurrency) {
                    answer[it.fromCurrency] = 1/it.exchangeRate
                }
            }

            return answer
        }

        override suspend fun getExchangeRate(fromCurrency: String, toCurrency: String): Double {
            return getExchangeRates(fromCurrency)[toCurrency]!!
        }

        override suspend fun getConversion(fromCurrency: String, toCurrency: String, amount: Double): Double {
            return getExchangeRate(fromCurrency, toCurrency) * amount
        }
    }

    private val dummyExchangeRateSaved = object : ExchangeRateSaved {
        private val savedValues = listOf(
                CurrencyRate("RUB", "RUB", 1.0, dateFormat.format(Calendar.getInstance().time)),
                CurrencyRate("USD", "RUB", 25.0, "1971-12-01 GMT"),
                CurrencyRate("USD", "EUR", 0.5, "1971-12-01 GMT"),
                CurrencyRate("EUR", "CUC", 1.25, dateFormat.format(Calendar.getInstance().time))
        )

        private val fromToCurrencyRates: MutableMap<Pair<String, String> , CurrencyRate> = mutableMapOf()

        init {
            savedValues.forEach {
                fromToCurrencyRates[Pair(it.fromCurrency, it.toCurrency)] = it
            }
        }

        override suspend fun getAllSavedExchangeRates(): List<CurrencyRate> {
            return savedValues
        }

        override suspend fun getExchangeRates(fromCurrency: String, toCurrencies: List<String>): List<CurrencyRate> {
            val answer: MutableList<CurrencyRate> = mutableListOf()
            toCurrencies.forEach {
                if (fromToCurrencyRates[Pair(fromCurrency, it)] != null) {
                    answer.add(fromToCurrencyRates[Pair(fromCurrency, it)]!!)
                }
            }

            return answer
        }

        override suspend fun insertExchangeRates(currencyRates: List<CurrencyRate>) {}

        override suspend fun updateExchangeRates(currencyRates: List<CurrencyRate>) {}

        override suspend fun deleteExchangeRates(currencyRates: List<CurrencyRate>) {
            throw RuntimeException("delete")
        }

    }
}