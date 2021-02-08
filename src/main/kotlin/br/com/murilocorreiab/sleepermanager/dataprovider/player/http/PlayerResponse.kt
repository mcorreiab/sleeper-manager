package br.com.murilocorreiab.sleepermanager.dataprovider.player.http

data class PlayerResponse(
    val playerId: String,
    val fullName: String?,
    val firstName: String,
    val lastName: String,
    val injuryStatus: String?,
    val position: String?,
    val team: String?
)
