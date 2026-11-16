package com.example.difzach

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class PokemonDetailFragment : Fragment(R.layout.fragment_pokemon_detail) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameOrId = arguments?.getString("pokemon_key")
            ?: PrefsHelper(requireContext()).getLastPokemon()
            ?: run {
                Toast.makeText(requireContext(), "Нет данных о покемоне", Toast.LENGTH_LONG).show()
                return
            }

        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvId = view.findViewById<TextView>(R.id.tvId)
        val ivPokemon = view.findViewById<ImageView>(R.id.ivPokemon)
        val progress = view.findViewById<ProgressBar>(R.id.progress)

        tvName.text = "Загрузка…"
        tvId.text = "#$nameOrId"
        progress.visibility = View.VISIBLE

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val pokemon = RetrofitClient.api.getPokemonDetail(nameOrId)

                tvName.text = pokemon.name.replaceFirstChar { it.uppercase() }
                tvId.text = "#${pokemon.id}"
                progress.visibility = View.GONE

                Glide.with(requireContext())
                    .load(pokemon.sprites?.front_default)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(ivPokemon)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                progress.visibility = View.GONE
                if (isAdded) {
                    tvName.text = "Ошибка"
                    Toast.makeText(requireContext(), "Не найдено: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}