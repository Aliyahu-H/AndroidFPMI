package com.example.petexchange.model.sources.exchangerate.exchangerate_api.receiver

import com.example.petexchange.model.sources.exchangerate.exchangerate_api.retrofit.ExchangeRateApi
import com.example.petexchange.model.sources.exchangerate.ExchangeRateReceiver
import com.example.petexchange.model.sources.exchangerate.exchangerate_api.retrofit.PairExchangeRateAnswer
import com.example.petexchange.model.sources.exchangerate.exchangerate_api.retrofit.StandardExchangeRateAnswer
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.Exception

class RetrofitExchangeRateReceiver() : ExchangeRateReceiver {
    private companion object {
        const val baseUrl = "https://v6.exchangerate-api.com/v6/"
        const val key = "b2f47f0d69f4877b5e0fb01f"
        var exchangeRateApi: ExchangeRateApi? = null
    }

    init {
        if (exchangeRateApi == null) {
            exchangeRateApi = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create())
                .build().create(ExchangeRateApi::class.java)
        }
    }

    override suspend fun getExchangeRates(fromCurrency: String): Map<String, Double> {
        val exchangeRates: StandardExchangeRateAnswer
        try {
            exchangeRates = exchangeRateApi!!.getExchangeRates(key, fromCurrency)
        } catch(e: Exception) {
            throw RuntimeException("Error occurred in request: " + e.message)
        }

        if (exchangeRates.conversionRates == null) {
            throw RuntimeException("Error occurred in ExchangeRate-API: " +
                    exchangeRates.errorType)
        }

        return exchangeRates.conversionRates
    }

    override suspend fun getExchangeRate(fromCurrency: String, toCurrency: String): Double {
        val exchangeRates: PairExchangeRateAnswer
        try {
            exchangeRates = exchangeRateApi!!.getExchangeRate(key, fromCurrency, toCurrency)
        } catch(e: Exception) {
            throw RuntimeException("Error occurred in request: " + e.message)
        }

        if (exchangeRates.result != "success") {
            throw RuntimeException("Error occurred in ExchangeRate-API: " +
                    exchangeRates.errorType)
        }

        return exchangeRates.conversionRate
    }

    override suspend  fun getConversion(
        fromCurrency: String,
        toCurrency: String,
        amount: Double
    ): Double {
        val exchangeRates: PairExchangeRateAnswer
        try {
            exchangeRates = exchangeRateApi!!.getExchangeRate(key, fromCurrency, toCurrency)
        } catch(e: Exception) {
            throw RuntimeException("Error occurred in request: " + e.message)
        }

        if (exchangeRates.result != "success") {
            throw RuntimeException("Error occurred in ExchangeRate-API: " +
                    exchangeRates.errorType)
        }

        return exchangeRates.conversionResult
    }
}