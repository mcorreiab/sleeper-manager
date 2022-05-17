package br.com.murilocorreiab.sleepermanager.entities.player

data class Player(
    val id: String,
    val name: String,
    val injuryStatus: String,
    val position: String,
    val team: Team,
)
