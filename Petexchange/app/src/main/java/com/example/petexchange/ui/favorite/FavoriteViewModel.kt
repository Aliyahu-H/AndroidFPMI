package com.example.petexchange.ui.favorite

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.petexchange.ui.currency.Currency
import com.example.petexchange.ui.currency.DataSourceFavorite
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class FavoriteViewModel(val dataSource: DataSourceFavorite) : ViewModel() {

    val currencyLiveData = dataSource.getCurrencyList()

    fun insertCurrency(currency: Currency?) {
        GlobalScope.launch {dataSource.addCurrency(currency)}
    }
}

class FavoriteViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            val favoriteViewModel = FavoriteViewModel(
                    dataSource = DataSourceFavorite.getDataSource(context.resources, context)
            )
            runBlocking {
                favoriteViewModel.dataSource.onCreate()
            }
            return favoriteViewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}