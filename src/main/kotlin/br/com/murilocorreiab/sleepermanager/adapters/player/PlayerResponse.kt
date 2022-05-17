package br.com.murilocorreiab.sleepermanager.adapters.player

data class PlayerResponse(
    val id: String,
    val name: String,
    val injuryStatus: String,
    val position: String,
    val team: String,
)
