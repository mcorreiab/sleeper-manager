package br.com.murilocorreiab.sleepermanager.entities.player

data class Player(
    override val id: String,
    val name: String,
    val injuryStatus: String,
    var starter: Boolean,
    val position: String,
    val team: Team,
) : Play
