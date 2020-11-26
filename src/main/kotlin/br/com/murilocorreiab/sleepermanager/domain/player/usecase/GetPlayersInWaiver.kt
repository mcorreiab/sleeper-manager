package br.com.murilocorreiab.sleepermanager.domain.player.usecase

import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import kotlinx.coroutines.flow.Flow

interface GetPlayersInWaiver {
    suspend fun get(username: String, playersToCheck: List<String>): Flow<Player>
}
