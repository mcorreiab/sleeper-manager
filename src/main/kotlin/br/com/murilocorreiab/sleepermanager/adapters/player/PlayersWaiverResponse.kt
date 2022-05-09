package br.com.murilocorreiab.sleepermanager.adapters.player

import br.com.murilocorreiab.sleepermanager.entities.league.model.League
import br.com.murilocorreiab.sleepermanager.entities.player.Player

data class PlayersWaiverResponse(val player: Player, val leagues: List<League>)
