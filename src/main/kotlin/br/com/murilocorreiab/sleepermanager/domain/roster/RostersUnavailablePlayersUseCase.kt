package br.com.murilocorreiab.sleepermanager.domain.roster

import br.com.murilocorreiab.sleepermanager.entities.league.League
import br.com.murilocorreiab.sleepermanager.entities.league.Roster2
import br.com.murilocorreiab.sleepermanager.entities.player.Player
import br.com.murilocorreiab.sleepermanager.entities.player.PlayerStatus
import br.com.murilocorreiab.sleepermanager.usecase.LeagueGateway
import br.com.murilocorreiab.sleepermanager.usecase.PlayerGateway
import br.com.murilocorreiab.sleepermanager.usecase.RosterGateway2
import jakarta.inject.Singleton

@Singleton
class RostersUnavailablePlayersUseCase(
    private val leagueGateway: LeagueGateway,
    private val rosterGateway2: RosterGateway2,
    private val playerGateway: PlayerGateway,
) {
    fun getByUserId(userId: String): List<Roster2> {
        val rosters = leagueGateway.findAllUserLeagues(userId).mapToRostersOfAUser(userId)
        val players = rosters.mapToStarterPlayers()
        return rosters.mapToRostersWithInactiveStartersFilled(players)
    }

    private fun List<League>.mapToRostersOfAUser(userId: String) =
        flatMap { rosterGateway2.findRostersOfLeague(it.id) }.filter { it.ownerId == userId }

    private fun List<Roster2>.mapToStarterPlayers() =
        flatMap { it.starters }.distinct().mapNotNull { playerGateway.getById(it.id) }.associateBy { it.id }

    private fun List<Roster2>.mapToRostersWithInactiveStartersFilled(players: Map<String, Player>) = mapNotNull {
        val unavailablePlayers = it.filterInactiveStarters(players)
        if (unavailablePlayers.isNotEmpty()) {
            it.copy(players = unavailablePlayers)
        } else {
            null
        }
    }

    private fun Roster2.filterInactiveStarters(players: Map<String, Player>) =
        starters.mapNotNull { players[it.id] }.filter { it.injuryStatus != PlayerStatus.ACTIVE.name }
}
