package br.com.murilocorreiab.sleepermanager.domain.player

import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import br.com.murilocorreiab.sleepermanager.domain.player.gateway.PlayerGateway
import br.com.murilocorreiab.sleepermanager.domain.roster.gateway.RosterGateway
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filter
import javax.inject.Singleton

@Singleton
class PlayerService(private val rosterGateway: RosterGateway, private val playerGateway: PlayerGateway) {
    suspend fun getPlayersInWaiver(username: String, playersToCheck: List<String>): Flow<Player> = coroutineScope {
        val rosteredPlayers = async {
            rosterGateway.findAllRosteredPlayersInUserLeagues(username)
        }

        val playersInformation = async {
            playerGateway.getPlayersInformation(playersToCheck)
        }

        playersInformation.await()
            .filter { player ->
                rosteredPlayers.await().count { rosteredPlayer -> rosteredPlayer.id == player.id } == 0
            }
    }
}
