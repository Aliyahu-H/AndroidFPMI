package com.example.petexchange.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.petexchange.R
import com.example.petexchange.ui.currency.Currency

class FavoriteViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is favorite Fragment"
    }

    private val _currencyList = (0..30).map{
        Currency(R.drawable.ic_dollar, "USD", .0)
    }

        val currencyList: List<Currency> = _currencyList
}