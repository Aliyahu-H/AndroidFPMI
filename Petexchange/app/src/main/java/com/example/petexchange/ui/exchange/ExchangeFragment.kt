package com.example.petexchange.ui.exchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.petexchange.R
import com.example.petexchange.databinding.FragmentExchangeBinding
import com.example.petexchange.ui.currency.Currency


class ExchangeFragment : Fragment() {

    private lateinit var exchangeViewModel: ExchangeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        exchangeViewModel =
                ViewModelProvider(this).get(ExchangeViewModel::class.java)
        val binding = FragmentExchangeBinding.inflate(inflater, container, false)
        val root = binding.root
        val currency1 = Currency(R.drawable.ic_dollar, "USD", .0)
        val currency2 = Currency(R.drawable.ic_euro, "EU", .0)

        binding.currency1 = currency1
        binding.currency2 = currency2
        return root
    }
}