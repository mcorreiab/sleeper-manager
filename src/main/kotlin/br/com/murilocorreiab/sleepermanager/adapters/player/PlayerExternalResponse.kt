package br.com.murilocorreiab.sleepermanager.adapters.player

data class PlayerExternalResponse(
    val playerId: String,
    val fullName: String?,
    val firstName: String,
    val lastName: String,
    val injuryStatus: String?,
    val position: String?,
    val team: String?
)
