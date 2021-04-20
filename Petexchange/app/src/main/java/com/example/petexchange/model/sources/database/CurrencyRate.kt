package com.example.petexchange.model.sources.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_rate", primaryKeys = ["from_currency","to_currency"])
data class CurrencyRate (
    @field:ColumnInfo(name = "from_currency")
    var fromCurrency: String,
    @field:ColumnInfo(name = "to_currency")
    var toCurrency: String,
    @field:ColumnInfo(name = "exchange_rate")
    var exchangeRate: Double,
    @field:ColumnInfo(name = "update_date")
    var updateDate: String
    )
{}