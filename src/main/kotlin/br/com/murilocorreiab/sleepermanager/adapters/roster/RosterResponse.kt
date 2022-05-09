package br.com.murilocorreiab.sleepermanager.adapters.roster

import br.com.murilocorreiab.sleepermanager.adapters.league.LeagueResponse
import br.com.murilocorreiab.sleepermanager.adapters.player.PlayerResponse

data class RosterResponse(
    val id: String,
    val ownerId: String?,
    val players: List<PlayerResponse>,
    val league: LeagueResponse
)
