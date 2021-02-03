package br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity

data class RosterResponse(
    val rosterId: String,
    val starters: List<String>,
    val players: List<String>,
    val ownerId: String?
)
