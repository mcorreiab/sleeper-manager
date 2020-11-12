package br.com.murilocorreiab.sleepermanager.domain.roster.entity

import br.com.murilocorreiab.sleepermanager.domain.league.entity.League

data class Roster(val id: String, val ownerId: String?, val players: List<Player>, val league: League)
