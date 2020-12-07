package br.com.murilocorreiab.sleepermanager.domain.player.gateway

import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import kotlinx.coroutines.flow.Flow

interface ModifyPlayerGateway {
    suspend fun updatePlayers(players: Flow<Player>): Flow<Player>
}
