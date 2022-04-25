package br.com.murilocorreiab.sleepermanager.domain.player.entity

data class Player(
    override val id: String,
    val name: String,
    val injuryStatus: String,
    var starter: Boolean,
    val position: String,
    val team: Team,
) : Play
