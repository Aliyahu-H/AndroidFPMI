package com.example.petexchange.ui.addCurrency

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petexchange.R
import com.example.petexchange.ui.currency.Currency
import com.example.petexchange.ui.currency.CurrencyAdapter
import com.example.petexchange.ui.favorite.FROM
import com.example.petexchange.ui.favorite.TO
import java.util.*

const val CURRENCY = "currency"

class AddCurrencyActivity : FragmentActivity() {
    private lateinit var addCurrencyViewModel: AddCurrencyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_currency_layout)
        addCurrencyViewModel =
                ViewModelProvider(this, AddCurrencyViewModelFactory(this)).get(AddCurrencyViewModel::class.java)
        val currencyAdapter = CurrencyAdapter(addCurrencyViewModel.currencyLiveData) { currency -> adapterOnClick(currency) }

        val recyclerView: RecyclerView = findViewById(R.id.addCurrencyRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = currencyAdapter
    }

    private fun adapterOnClick(currency: Currency?) {
        val new_intent = Intent()
        new_intent.putExtra(CURRENCY, currency)
        new_intent.putExtra(FROM, intent?.getBooleanExtra(FROM, false))
        new_intent.putExtra(TO, intent?.getBooleanExtra(TO, false))
        setResult(RESULT_OK, new_intent)
        finish()
    }
}