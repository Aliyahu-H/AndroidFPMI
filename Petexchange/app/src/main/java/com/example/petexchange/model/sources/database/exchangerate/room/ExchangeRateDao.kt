package com.example.petexchange.model.sources.database.exchangerate.room

import androidx.room.*
import com.example.petexchange.model.sources.database.CurrencyRate
import com.example.petexchange.model.sources.database.exchangerate.ExchangeRateSaved


@Dao
interface ExchangeRateDao {
    @Query("SELECT * FROM currency_rate")
    suspend fun getAll() : List<CurrencyRate>

    @Query("SELECT * FROM currency_rate WHERE from_currency = :fromCurrency")
    suspend fun loadAllExchangeRates(fromCurrency: String) : List<CurrencyRate>

    @Query("SELECT * FROM currency_rate " +
                "WHERE (from_currency = :fromCurrency AND to_currency IN (:toCurrencies))")
    suspend fun loadExchangeRates(fromCurrency: String, toCurrencies: List<String>) : List<CurrencyRate>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currencyRates: List<CurrencyRate>)

    @Update
    suspend fun update(currencyRates: List<CurrencyRate>)

    @Delete
    suspend fun delete(currencyRates: List<CurrencyRate>)
}