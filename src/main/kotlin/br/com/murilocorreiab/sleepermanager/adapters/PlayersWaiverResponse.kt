package br.com.murilocorreiab.sleepermanager.adapters

import br.com.murilocorreiab.sleepermanager.entities.league.League
import br.com.murilocorreiab.sleepermanager.entities.player.Player

data class PlayersWaiverResponse(val player: Player, val leagues: List<League>)
