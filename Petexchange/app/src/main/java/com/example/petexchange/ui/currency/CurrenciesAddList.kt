package com.example.petexchange.ui.currency

import android.content.res.Resources
import com.example.petexchange.R

/* Returns initial list of currencies. */
fun addCurrencyList(resources: Resources): MutableSet<Currency> {
    return mutableSetOf(
        Currency(R.drawable.ic_coin, "USD","USD",  1.0),
        Currency(R.drawable.ic_coin, "EUR","EUR", 1.0),
        Currency(R.drawable.ic_coin, "CZK","CZK", 1.0),
        Currency(R.drawable.ic_coin, "AED","AED", 1.0),
        Currency(R.drawable.ic_coin, "AUD","AUD", 1.0),
        Currency(R.drawable.ic_coin, "BRL","BRL", 1.0),
        Currency(R.drawable.ic_coin, "CUP","CUP", 1.0),
        Currency(R.drawable.ic_coin, "CUC","CUC", 1.0),
        Currency(R.drawable.ic_coin, "GIP","GIP", 1.0),
        Currency(R.drawable.ic_coin, "IDR","IDR", 1.0),
        Currency(R.drawable.ic_coin, "IQD","IQD", 1.0),
        Currency(R.drawable.ic_coin, "KRW","KRW", 1.0),
        Currency(R.drawable.ic_coin, "PLN","PLN", 1.0),
        Currency(R.drawable.ic_coin, "SSP","SSP", 1.0),
        Currency(R.drawable.ic_coin, "THB","THB", 1.0),
            Currency(R.drawable.ic_coin, "RUB","RUB", 1.0)
    )
}