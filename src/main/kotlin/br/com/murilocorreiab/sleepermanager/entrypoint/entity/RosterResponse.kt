package br.com.murilocorreiab.sleepermanager.entrypoint.entity

data class RosterResponse(
    val id: String,
    val ownerId: String?,
    val players: List<PlayerResponse>,
    val league: LeagueResponse
)
