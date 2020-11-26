package br.com.murilocorreiab.sleepermanager.domain.player.usecase

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
class GetPlayersInWaiverUseCase(private val rosterGateway: RosterGateway, private val playerGateway: PlayerGateway) :
    GetPlayersInWaiver {
    override suspend fun get(username: String, players: List<String>): Flow<Player> = coroutineScope {
        val rosteredPlayers = async {
            rosterGateway.findAllRosteredPlayersInUserLeagues(username)
        }

        val playersToCheck = async {
            playerGateway.getPlayersInformation(players)
        }

        playersToCheck.await()
            .filter { checkIfPlayerIsRostered(rosteredPlayers.await(), it) }
    }

    private suspend fun checkIfPlayerIsRostered(
        rosteredPlayers: Flow<Player>,
        player: Player
    ) = rosteredPlayers.count { it.id == player.id } == 0
}
