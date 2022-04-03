package br.com.murilocorreiab.sleepermanager.domain.player.entity

import br.com.murilocorreiab.sleepermanager.domain.league.entity.League

data class LeaguesForPlayer(val player: Player, val leagues: List<League>)
