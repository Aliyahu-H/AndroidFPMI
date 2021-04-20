package com.example.petexchange.model.sources.database.exchangerate.room

import android.content.Context
import androidx.room.Room
import com.example.petexchange.model.sources.database.CurrencyRate
import com.example.petexchange.model.sources.database.exchangerate.ExchangeRateSaved


class RoomExchangeRateSaved(context: Context) : ExchangeRateSaved {
    private companion object {
        var exchangeRateDatabase: ExchangeRateDatabase? = null
    }

    init {
        if (exchangeRateDatabase == null) {
            synchronized(ExchangeRateDatabase::class) {
                exchangeRateDatabase = Room.databaseBuilder(
                    context.applicationContext,
                    ExchangeRateDatabase::class.java, "currency_rate"
                ).build()
            }
        }
    }

    override suspend fun getAllSavedExchangeRates(): List<CurrencyRate> {
        return exchangeRateDatabase?.exchangeRateDao()?.getAll()?: listOf()
    }

    override suspend fun getExchangeRates(
        fromCurrency: String,
        toCurrencies: List<String>
    ): List<CurrencyRate> {
        return exchangeRateDatabase?.exchangeRateDao()?.loadExchangeRates(fromCurrency, toCurrencies)?: listOf()
    }

    override suspend fun insertExchangeRates(currencyRates: List<CurrencyRate>) {
        exchangeRateDatabase?.exchangeRateDao()?.insert(currencyRates)
    }

    override suspend fun updateExchangeRates(currencyRates: List<CurrencyRate>) {
        exchangeRateDatabase?.exchangeRateDao()?.update(currencyRates)
    }

    override suspend fun deleteExchangeRates(currencyRates: List<CurrencyRate>) {
        exchangeRateDatabase?.exchangeRateDao()?.delete(currencyRates)
    }
}