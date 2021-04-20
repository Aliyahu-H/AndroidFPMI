package com.example.petexchange.model.converters

import com.example.petexchange.model.sources.database.CurrencyRate
import com.example.petexchange.model.sources.database.exchangerate.ExchangeRateSaved
import com.example.petexchange.model.sources.exchangerate.ExchangeRateReceiver
import java.text.SimpleDateFormat
import java.util.*

class OneDayConverter(
    private val exchangeRateReceiver: ExchangeRateReceiver,
    private val exchangeRateSaved: ExchangeRateSaved
) : Converter {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.getDefault())

    override suspend fun convert(fromCurrency: String, toCurrency: String, amount: Double): Double {
        val savedCurrencyRates: MutableList<CurrencyRate> = exchangeRateSaved.getExchangeRates(
            fromCurrency,
            listOf(toCurrency)
        ).toMutableList()
        var convertedAmount = .0
        when {
            savedCurrencyRates.isEmpty() -> {
                exchangeRateReceiver.getConversion(fromCurrency, toCurrency, amount)
            }
            checkDate(savedCurrencyRates[0].updateDate) -> {
                savedCurrencyRates[0].exchangeRate = exchangeRateReceiver.getExchangeRate(fromCurrency, toCurrency)
                savedCurrencyRates[0].updateDate = dateFormat.format(Calendar.getInstance().time)
                exchangeRateSaved.updateExchangeRates(savedCurrencyRates)
                convertedAmount = savedCurrencyRates[0].exchangeRate*amount
            }
            else -> {
                convertedAmount = savedCurrencyRates[0].exchangeRate*amount
            }
        }

        return convertedAmount
    }

    override suspend fun getCurrencyRate(fromCurrency: String, toCurrency: String): Double {
        val savedCurrencyRates: MutableList<CurrencyRate> = exchangeRateSaved.getExchangeRates(
            fromCurrency,
            listOf(toCurrency)
        ).toMutableList()

        when {
            savedCurrencyRates.isEmpty() -> {
                savedCurrencyRates.add(CurrencyRate(fromCurrency,
                    toCurrency,
                    exchangeRateReceiver.getExchangeRate(fromCurrency, toCurrency),
                    dateFormat.format(Calendar.getInstance().time)))
                exchangeRateSaved.insertExchangeRates(savedCurrencyRates)
            }
            checkDate(savedCurrencyRates[0].updateDate) -> {
                savedCurrencyRates[0].exchangeRate = exchangeRateReceiver.getExchangeRate(fromCurrency, toCurrency)
                savedCurrencyRates[0].updateDate = dateFormat.format(Calendar.getInstance().time)
                exchangeRateSaved.updateExchangeRates(savedCurrencyRates)
            }
        }

        return savedCurrencyRates[0].exchangeRate
    }

    override suspend fun getCurrencyRates(
        fromCurrency: String,
        toCurrencies: List<String>
    ): List<Double> {
        val savedExchangeRates: MutableList<CurrencyRate> = exchangeRateSaved.getExchangeRates(
            fromCurrency,
            toCurrencies
        ).toMutableList()

        if ((savedExchangeRates.size < toCurrencies.size) or
                savedExchangeRates.any{ checkDate(it.updateDate) }) {
            savedExchangeRates.clear()
            val newExchangeRates = exchangeRateReceiver.getExchangeRates(fromCurrency)
            toCurrencies.forEach {
                savedExchangeRates.add(
                    CurrencyRate(
                        fromCurrency,
                        it,
                        newExchangeRates[it]?: -1.0,
                        dateFormat.format(Calendar.getInstance().time)
                    )
                )
            }

            exchangeRateSaved.insertExchangeRates(savedExchangeRates)
        }

        return List<Double>(toCurrencies.size) { savedExchangeRates[it].exchangeRate }
    }

    override suspend fun loadSavedExchangeRates(): List<CurrencyRate> {
        return exchangeRateSaved.getAllSavedExchangeRates()
    }

    override suspend fun deleteExchangeRates(fromCurrency: String, toCurrencies: List<String>) {
        exchangeRateSaved.deleteExchangeRates(List<CurrencyRate>(toCurrencies.size) {
            CurrencyRate(
                fromCurrency,
                toCurrencies[it],
                .0,
                ""
            )
        })
    }

    private fun checkDate(updateDate: String) : Boolean {
        val currentDate: Calendar = Calendar.getInstance()
        val nextUpdateDate: Calendar = Calendar.getInstance()
        nextUpdateDate.time = dateFormat.parse(updateDate)?: return true
        nextUpdateDate.add(Calendar.DATE, 1)
        return nextUpdateDate < currentDate
    }
}