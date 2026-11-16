package com.example.difzach

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class PokemonListFragment : Fragment(R.layout.fragment_pokemon_list) {

    private lateinit var prefsHelper: PrefsHelper
    private lateinit var adapter: PokemonAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefsHelper = PrefsHelper(requireContext())

        val rv = view.findViewById<RecyclerView>(R.id.rvPokemon)
        val etSearch = view.findViewById<EditText>(R.id.etSearch)
        val btnSearch = view.findViewById<Button>(R.id.btnSearch)

        adapter = PokemonAdapter { pokemon -> openDetail(pokemon.name) }
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        loadPokemonList()

        btnSearch.setOnClickListener {
            val query = etSearch.text.toString().trim()
            if (query.isNotEmpty()) openDetail(query)
            else Toast.makeText(requireContext(), "Введите имя или ID", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadPokemonList() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getPokemonList(limit = 30, offset = 0)
                adapter.updateItems(response.results)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Ошибка сети: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun openDetail(nameOrId: String) {
        prefsHelper.saveLastPokemon(nameOrId)

        val bundle = Bundle().apply { putString("pokemon_key", nameOrId) }
        val detail = PokemonDetailFragment().apply { arguments = bundle }

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, detail)
            .addToBackStack(null)
            .commit()
    }
}