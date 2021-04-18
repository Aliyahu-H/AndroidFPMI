package com.example.petexchange.ui.exchange

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.petexchange.ui.currency.Currency

class ExchangeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is exchange Fragment"
    }

    val text: LiveData<String> = _text
}