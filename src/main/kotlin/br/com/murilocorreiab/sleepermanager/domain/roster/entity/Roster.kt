package br.com.murilocorreiab.sleepermanager.domain.roster.entity

import br.com.murilocorreiab.sleepermanager.entities.league.League
import br.com.murilocorreiab.sleepermanager.entities.player.Player

data class Roster(val id: String, val ownerId: String?, val players: List<Player>, val league: League)
