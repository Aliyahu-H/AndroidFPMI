package com.example.petexchange.model.sources.exchangerate.exchangerate_api.retrofit

import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateApi {
    @GET("{key}/latest/{currency}")
    suspend fun getExchangeRates(
            @Path("key") key: String,
            @Path("currency")  currency: String
        ) : StandardExchangeRateAnswer

    @GET("{key}/pair/{from_currency}/{to_currency}")
    suspend fun getExchangeRate(
        @Path("key") key: String,
        @Path("from_currency") fromCurrency: String,
        @Path("to_currency")  toCurrency: String
    ) : PairExchangeRateAnswer

    @GET("{key}/pair/{from_currency}/{to_currency}/{amount}")
    suspend fun getConverting(
        @Path("key") key: String,
        @Path("from_currency") fromCurrency: String,
        @Path("to_currency")  toCurrency: String,
        @Path("amount") amount: Double
    ) : PairExchangeRateAnswer
}