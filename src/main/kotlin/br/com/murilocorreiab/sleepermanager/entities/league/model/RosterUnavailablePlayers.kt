package br.com.murilocorreiab.sleepermanager.entities.league.model

import br.com.murilocorreiab.sleepermanager.entities.player.Player

data class RosterUnavailablePlayers(val id: String, val ownerId: String?, val players: List<Player>, val league: League)
