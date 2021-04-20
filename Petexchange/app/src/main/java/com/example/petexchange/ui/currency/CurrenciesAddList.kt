package com.example.petexchange.ui.currency

import android.content.res.Resources
import com.example.petexchange.R

/* Returns initial list of currencies. */
fun addCurrencyList(resources: Resources): MutableSet<Currency> {
    return mutableSetOf(
        Currency(R.drawable.ic_dollar, "RUB","USD",  .0),
        Currency(R.drawable.ic_euro, "RUB","EUR", .0),
        Currency(R.drawable.ic_ruble, "RUB","RUB", .0)
    )
}