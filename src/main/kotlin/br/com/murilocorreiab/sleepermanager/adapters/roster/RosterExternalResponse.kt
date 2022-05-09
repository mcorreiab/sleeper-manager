package br.com.murilocorreiab.sleepermanager.adapters.roster

data class RosterExternalResponse(
    val rosterId: String,
    val starters: List<String>,
    val players: List<String>?,
    val ownerId: String?,
    val leagueId: String,
)
