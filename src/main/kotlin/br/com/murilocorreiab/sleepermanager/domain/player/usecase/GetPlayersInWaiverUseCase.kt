package br.com.murilocorreiab.sleepermanager.domain.player.usecase

import br.com.murilocorreiab.sleepermanager.domain.league.entity.League
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import br.com.murilocorreiab.sleepermanager.domain.player.gateway.GetPlayersGateway
import br.com.murilocorreiab.sleepermanager.domain.roster.gateway.RosterGateway
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Singleton

@Singleton
class GetPlayersInWaiverUseCase(
    private val rosterGateway: RosterGateway,
    private val getPlayersGateway: GetPlayersGateway
) :
    GetPlayersInWaiver {
    override suspend fun get(username: String, players: List<String>): Flow<Pair<Player, Flow<League>>> =
        coroutineScope {
            val rosteredPlayers = async {
                rosterGateway.findAllRosteredPlayersInUserLeagues(username)
            }

            val playersToCheck = async {
                getPlayersGateway.getPlayersInformation(players)
            }

            playersToCheck.await().mapNotNull {
                val leaguesWithPlayerAvailable = getLeaguesWithOneOfPlayersInWaiver(rosteredPlayers.await(), it)
                if (leaguesWithPlayerAvailable.count() > 0) {
                    Pair(it, leaguesWithPlayerAvailable)
                } else {
                    null
                }
            }
        }

    private suspend fun getLeaguesWithOneOfPlayersInWaiver(
        rosteredPlayers: Flow<Pair<League, Flow<Player>>>,
        player: Player
    ): Flow<League> = rosteredPlayers
        .filter { (_, rosters) -> checkIfPlayerIsNotRostered(rosters, player) }
        .map { (league, _) -> league }

    private suspend fun checkIfPlayerIsNotRostered(
        rosteredPlayers: Flow<Player>,
        player: Player
    ) = rosteredPlayers.count { it.id == player.id } == 0
}
