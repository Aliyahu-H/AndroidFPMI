package com.example.petexchange.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petexchange.R
import com.example.petexchange.ui.currency.CurrencyAdapter

class FavoriteFragment : Fragment() {

    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        favoriteViewModel =
                ViewModelProvider(this, FavoriteViewModelFactory(this.requireContext())).get(FavoriteViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_favorite, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.favoriteRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(root.context)
        var adapter = CurrencyAdapter(favoriteViewModel.currencyLiveData) {}
        recyclerView.adapter = adapter
        favoriteViewModel.currencyLiveData.observe(viewLifecycleOwner, {
            adapter.notifyDataSetChanged()
        })
        return root
    }
}