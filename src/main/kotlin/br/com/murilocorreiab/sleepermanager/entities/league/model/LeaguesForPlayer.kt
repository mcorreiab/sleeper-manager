package br.com.murilocorreiab.sleepermanager.entities.league.model

import br.com.murilocorreiab.sleepermanager.entities.player.Player

data class LeaguesForPlayer(val player: Player, val leagues: List<League>)
