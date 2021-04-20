package com.example.petexchange.ui.currency

import android.content.res.Resources
import com.example.petexchange.R

/* Returns initial list of currencies. */
fun addCurrencyList(resources: Resources): Set<Currency> {
    return setOf(
        Currency(R.drawable.ic_dollar, "USD", .0),
        Currency(R.drawable.ic_euro, "EU", .0),
        Currency(R.drawable.ic_ruble, "RUB", .0)
    )
}