package br.com.murilocorreiab.sleepermanager.domain.player.gateway

import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import kotlinx.coroutines.flow.Flow

interface PlayerGateway {

    suspend fun getPlayersInformation(players: List<String>): Flow<Player>
}
