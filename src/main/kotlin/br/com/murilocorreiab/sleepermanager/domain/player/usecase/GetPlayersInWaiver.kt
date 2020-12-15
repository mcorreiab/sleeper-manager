package br.com.murilocorreiab.sleepermanager.domain.player.usecase

import br.com.murilocorreiab.sleepermanager.domain.league.entity.League
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player

interface GetPlayersInWaiver {
    suspend fun get(username: String, players: List<String>): List<Pair<Player, List<League>>>
}
