package br.com.murilocorreiab.sleepermanager.entrypoint.entity

import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player

data class RosterResponse(val id: String, val ownerId: String?, val players: List<Player>, val league: LeagueResponse)
