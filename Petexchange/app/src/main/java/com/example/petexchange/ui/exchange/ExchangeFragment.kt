package com.example.petexchange.ui.exchange

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petexchange.R
import com.example.petexchange.ui.addCurrency.AddCurrencyActivity
import com.example.petexchange.ui.addCurrency.CURRENCY
import com.example.petexchange.ui.currency.Currency
import com.example.petexchange.ui.currency.CurrencyAdapter
import com.example.petexchange.ui.favorite.FROM
import com.example.petexchange.ui.favorite.TO


class ExchangeFragment : Fragment() {

    private lateinit var exchangeViewModel: ExchangeViewModel
    private val newCurrencyActivityRequestCode = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        exchangeViewModel =
            ViewModelProvider(this, ExchangeViewModelFactory(this.requireContext())).get(
                ExchangeViewModel::class.java)
        //val binding = FragmentExchangeBinding.inflate(inflater, container, false)
        //val root = binding.root
        //val currency1 = Currency(R.drawable.ic_dollar, "USD", .0)
        //val currency2 = Currency(R.drawable.ic_euro, "EU", .0)

        //binding.currency1 = currency1
        //binding.currency2 = currency2

        val root = inflater.inflate(R.layout.fragment_exchange, container, false)
        val fromRecyclerView: RecyclerView = root.findViewById(R.id.exchangeFromRecyclerView)
        fromRecyclerView.layoutManager = LinearLayoutManager(root.context)
        val adapterFrom = CurrencyAdapter(exchangeViewModel.currencyFromLiveData) {currency -> adapterFromOnClick(currency)}
        fromRecyclerView.adapter = adapterFrom
        exchangeViewModel.currencyFromLiveData.observe(viewLifecycleOwner, {
            adapterFrom.notifyDataSetChanged()
        })
        val toRecyclerView: RecyclerView = root.findViewById(R.id.exchangeToRecyclerView)
        toRecyclerView.layoutManager = LinearLayoutManager(root.context)
        val adapterTo = CurrencyAdapter(exchangeViewModel.currencyToLiveData) {currency -> adapterToOnClick(currency)}
        toRecyclerView.adapter = adapterTo
        exchangeViewModel.currencyToLiveData.observe(viewLifecycleOwner, {
            adapterTo.notifyDataSetChanged()
        })
        return root
    }

    private fun adapterFromOnClick(currency: Currency?) {
        val intent = Intent(activity, AddCurrencyActivity::class.java)
        intent.putExtra(FROM, true)
        intent.putExtra(TO, false)
        startActivityForResult(intent, newCurrencyActivityRequestCode)
    }

    private fun adapterToOnClick(currency: Currency?) {
        val intent = Intent(activity, AddCurrencyActivity::class.java)
        intent.putExtra(FROM, false)
        intent.putExtra(TO, true)
        startActivityForResult(intent, newCurrencyActivityRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        /* Inserts flower into viewModel. */
        if (requestCode == newCurrencyActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val currency: Currency? = intentData?.getParcelableExtra(CURRENCY)
            val from: Boolean? = intentData?.getBooleanExtra(FROM, false)
            val to: Boolean? = intentData?.getBooleanExtra(TO, false)

            if (from!!) {
                exchangeViewModel.changeFromCurrency(currency)
            }
            if (to!!) {
                exchangeViewModel.changeToCurrency(currency)
            }
        }
    }
}