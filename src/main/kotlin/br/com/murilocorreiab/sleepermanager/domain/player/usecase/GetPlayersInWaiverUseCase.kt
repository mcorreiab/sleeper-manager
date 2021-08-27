package br.com.murilocorreiab.sleepermanager.domain.player.usecase

import br.com.murilocorreiab.sleepermanager.domain.league.entity.League
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import br.com.murilocorreiab.sleepermanager.domain.player.gateway.GetPlayersGateway
import br.com.murilocorreiab.sleepermanager.domain.roster.gateway.RosterGateway
import jakarta.inject.Singleton
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

@Singleton
class GetPlayersInWaiverUseCase(
    private val rosterGateway: RosterGateway,
    private val getPlayersGateway: GetPlayersGateway
) :
    GetPlayersInWaiver {
    override suspend fun get(username: String, players: List<String>): List<Pair<Player, List<League>>> =
        coroutineScope {
            val rosteredPlayers = async {
                rosterGateway.findAllRosteredPlayersInUserLeagues(username)
            }

            val playersToCheck = async {
                getPlayersGateway.getPlayersInformation(players)
            }

            playersToCheck.await().mapNotNull {
                val leaguesWithPlayerAvailable = getLeaguesWithOneOfPlayersInWaiver(rosteredPlayers.await(), it)
                if (leaguesWithPlayerAvailable.isNotEmpty()) {
                    Pair(it, leaguesWithPlayerAvailable)
                } else {
                    null
                }
            }
        }

    private fun getLeaguesWithOneOfPlayersInWaiver(
        rosteredPlayers: List<Pair<League, List<Player>>>,
        player: Player
    ): List<League> = rosteredPlayers
        .filter { (_, rosters) -> checkIfPlayerIsNotRostered(rosters, player) }
        .map { (league, _) -> league }

    private fun checkIfPlayerIsNotRostered(
        rosteredPlayers: List<Player>,
        player: Player
    ) = rosteredPlayers.count { it.id == player.id } == 0
}
