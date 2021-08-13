package br.com.murilocorreiab.sleepermanager.entrypoint.entity

data class PlayerResponse(
    val id: String,
    val name: String,
    val injuryStatus: String,
    var starter: Boolean,
    val position: String,
    val team: String,
)
