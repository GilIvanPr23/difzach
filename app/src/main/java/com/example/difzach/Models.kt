package com.example.difzach

data class Pokemon(
    val id: Int,
    val name: String,
    val sprites: Sprites? = null
)

data class Sprites(
    val front_default: String?
)

data class PokemonListResponse(
    val results: List<PokemonResult>
)

data class PokemonResult(
    val name: String,
    val url: String
) {
    val id: Int
        get() = url.trimEnd('/').split("/").last().toIntOrNull() ?: 0
}