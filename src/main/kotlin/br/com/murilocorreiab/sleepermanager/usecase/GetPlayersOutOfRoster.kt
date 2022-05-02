package br.com.murilocorreiab.sleepermanager.usecase

import br.com.murilocorreiab.sleepermanager.entities.league.LeagueWithRosters
import br.com.murilocorreiab.sleepermanager.entities.league.getOutOfRosters
import br.com.murilocorreiab.sleepermanager.entities.league.model.LeaguesForPlayer
import br.com.murilocorreiab.sleepermanager.entities.player.Players
import jakarta.inject.Singleton

@Singleton
class GetPlayersOutOfRoster(
    private val leagueGateway: LeagueGateway,
    private val playerGateway: PlayerGateway,
    private val rosterGateway: RosterGateway2,
) {

    fun get(userId: String, nameFilters: List<String>): List<LeaguesForPlayer> {
        val notBestBallLeagues = GetOnlyStandardLeagues(leagueGateway).get(userId)
        val leaguesWithRosters = notBestBallLeagues.map {
            LeagueWithRosters(it, rosterGateway.findRostersOfLeague(it.id))
        }

        val players = playerGateway.getAllPlayers()
        // TODO: Put everything in only one function
        return Players(players).filterByName(nameFilters).getOutOfRosters(leaguesWithRosters)
    }
}
