package com.example.petexchange.model.converters

import com.example.petexchange.model.sources.database.CurrencyRate

interface Converter {
    suspend fun convert(fromCurrency: String, toCurrency: String, amount: Double): Double
    suspend fun getCurrencyRate(fromCurrency: String, toCurrency: String): Double
    suspend fun getCurrencyRates(fromCurrency: String, toCurrencies: List<String>): List<Double>

    suspend fun loadSavedExchangeRates(): List<CurrencyRate>
    suspend fun deleteExchangeRates(fromCurrency: String, toCurrencies: List<String>)
}