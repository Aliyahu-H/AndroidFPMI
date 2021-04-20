package com.example.petexchange.model.sources.database.exchangerate.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.petexchange.model.sources.database.CurrencyRate


@Database(entities = [CurrencyRate::class], version = 1)
abstract class ExchangeRateDatabase : RoomDatabase() {
    abstract fun exchangeRateDao(): ExchangeRateDao?
}
