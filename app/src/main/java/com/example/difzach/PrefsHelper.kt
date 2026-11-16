package com.example.difzach

import android.content.Context

class PrefsHelper(context: Context) {
    private val prefs = context.getSharedPreferences("poke_prefs", Context.MODE_PRIVATE)

    fun saveLastPokemon(nameOrId: String) =
        prefs.edit().putString("last_pokemon", nameOrId).apply()

    fun getLastPokemon(): String? =
        prefs.getString("last_pokemon", null)
}