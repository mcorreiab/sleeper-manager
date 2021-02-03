package br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity

data class LeagueResponse(
    val name: String,
    val leagueId: String,
    val totalRosters: Int,
    val avatar: String?
)
