package br.com.murilocorreiab.sleepermanager.application.http.league.entity

data class LeagueResponse(
        val name: String,
        val leagueId: Long,
        val totalRosters: Int
)