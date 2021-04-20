package com.example.petexchange.model.sources.exchangerate

interface ExchangeRateReceiver {
    suspend fun getExchangeRates(fromCurrency: String) : Map<String, Double>
    suspend fun getExchangeRate(fromCurrency: String, toCurrency: String) : Double
    suspend fun getConversion(fromCurrency: String, toCurrency: String, amount: Double) : Double
}