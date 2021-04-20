package com.example.petexchange.ui.favorite

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.petexchange.R
import com.example.petexchange.ui.currency.Currency
import com.example.petexchange.ui.currency.DataSourceFavorite
import kotlin.random.Random

class FavoriteViewModel(val dataSource: DataSourceFavorite) : ViewModel() {

    val currencyLiveData = dataSource.getCurrencyList()

    fun insertCurrency(currency: Currency?) {
        dataSource.addCurrency(currency)
    }
}

class FavoriteViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoriteViewModel(
                    dataSource = DataSourceFavorite.getDataSource(context.resources)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}