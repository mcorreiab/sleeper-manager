package br.com.murilocorreiab.sleepermanager.entrypoint.model

import br.com.murilocorreiab.sleepermanager.domain.league.entity.League
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player

data class PlayersWaiverResponse(val player: Player, val leagues: List<League>)
