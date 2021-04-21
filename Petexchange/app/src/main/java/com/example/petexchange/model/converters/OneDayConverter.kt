package com.example.petexchange.model.converters

import com.example.petexchange.model.sources.database.CurrencyRate
import com.example.petexchange.model.sources.database.exchangerate.ExchangeRateSaved
import com.example.petexchange.model.sources.exchangerate.ExchangeRateReceiver
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class OneDayConverter(
    private val exchangeRateReceiver: ExchangeRateReceiver,
    private val exchangeRateSaved: ExchangeRateSaved
) : Converter {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd z", Locale.getDefault())

    override suspend fun convert(fromCurrency: String, toCurrency: String, amount: Double): Double {
        val savedCurrencyRates: MutableList<CurrencyRate> = exchangeRateSaved.getExchangeRates(
            fromCurrency,
            listOf(toCurrency)
        ).toMutableList()
        var convertedAmount = amount
        if (fromCurrency != toCurrency) {
            when {
                savedCurrencyRates.isEmpty() -> {
                    convertedAmount = exchangeRateReceiver.getConversion(fromCurrency, toCurrency, amount)
                }
                checkDate(savedCurrencyRates[0].updateDate) -> {
                    savedCurrencyRates[0].exchangeRate =
                        exchangeRateReceiver.getExchangeRate(fromCurrency, toCurrency)
                    savedCurrencyRates[0].updateDate =
                        dateFormat.format(Calendar.getInstance().time)
                    exchangeRateSaved.updateExchangeRates(savedCurrencyRates)
                    convertedAmount = savedCurrencyRates[0].exchangeRate * amount
                }
                else -> {
                    convertedAmount = savedCurrencyRates[0].exchangeRate * amount
                }
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
                var exchangeRate = 1.0
                if (fromCurrency != toCurrency) {
                    exchangeRate = exchangeRateReceiver.getExchangeRate(fromCurrency, toCurrency)
                }
                savedCurrencyRates.add(CurrencyRate(fromCurrency,
                    toCurrency,
                    exchangeRate,
                    dateFormat.format(Calendar.getInstance().time)))
                exchangeRateSaved.insertExchangeRates(savedCurrencyRates)
            }
            (fromCurrency != toCurrency) and checkDate(savedCurrencyRates[0].updateDate) -> {
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
                savedExchangeRates.any{
                    checkDate(it.updateDate) and (it.fromCurrency != it.toCurrency)
                }) {
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
        val savedExchangeRates: MutableList<CurrencyRate> = exchangeRateSaved.getAllSavedExchangeRates().toMutableList()
        val toCurrencyRates: MutableMap<String, MutableList<CurrencyRate>> = mutableMapOf()
        val toUpdateToCurrencies: MutableSet<String> = mutableSetOf()

        savedExchangeRates.forEach {
            toCurrencyRates[it.toCurrency]?.add(it) ?: toCurrencyRates.put(it.toCurrency, mutableListOf(it))
            if (checkDate(it.updateDate) and (it.fromCurrency != it.toCurrency)) {
                toUpdateToCurrencies.add(it.toCurrency)
            }
        }

        toUpdateToCurrencies.forEach { toName ->
            val newExchangeRates = exchangeRateReceiver.getExchangeRates(toName)
            toCurrencyRates[toName]?.forEach {
                it.exchangeRate = (10000.0 / (newExchangeRates[it.fromCurrency]
                    ?: -1.0)).roundToInt() / 10000.0
                it.updateDate = dateFormat.format(Calendar.getInstance().time)
            }

            exchangeRateSaved.updateExchangeRates(toCurrencyRates[toName]?: listOf())
        }

        return savedExchangeRates
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