package br.com.murilocorreiab.sleepermanager.entities.league.model

data class Roster2(
    val id: String,
    val ownerId: String?,
    val players: List<String>,
    val starters: List<String>,
    val leagueId: String,
)
