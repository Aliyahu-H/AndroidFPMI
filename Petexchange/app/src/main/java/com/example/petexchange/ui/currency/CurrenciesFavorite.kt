package com.example.petexchange.ui.currency

import android.content.res.Resources
import com.example.petexchange.R

/* Returns initial list of currencies. */
fun favoriteCurrencyList(resources: Resources): Set<Currency> {
    return setOf(
            Currency(R.drawable.ic_dollar, "RUB","USD", .0)
    )
}