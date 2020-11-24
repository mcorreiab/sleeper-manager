package br.com.murilocorreiab.sleepermanager.domain.roster.entity

import br.com.murilocorreiab.sleepermanager.domain.league.entity.League
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player

data class Roster(val id: String, val ownerId: String?, val players: List<Player>, val league: League)
