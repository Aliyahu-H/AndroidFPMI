package com.example.petexchange.model.sources.database.exchangerate

import com.example.petexchange.model.sources.database.CurrencyRate

interface ExchangeRateSaved {
    suspend fun getAllSavedExchangeRates(): List<CurrencyRate>
    suspend fun getExchangeRates(fromCurrency: String, toCurrencies: List<String>): List<CurrencyRate>

    /**If an element exists, insert should work as update*/
    suspend fun insertExchangeRates(currencyRates: List<CurrencyRate>)
    suspend fun updateExchangeRates(currencyRates: List<CurrencyRate>)
    suspend fun deleteExchangeRates(currencyRates: List<CurrencyRate>)
}