package br.com.murilocorreiab.sleepermanager.usecase

import br.com.murilocorreiab.sleepermanager.entities.league.LeagueWithRosters
import br.com.murilocorreiab.sleepermanager.entities.league.model.LeaguesForPlayer
import br.com.murilocorreiab.sleepermanager.entities.player.FilterPlayers
import jakarta.inject.Singleton

@Singleton
class GetPlayersOutOfRoster(
    private val leagueGateway: LeagueGateway,
    private val playerGateway: PlayerGateway,
    private val rosterGateway: RosterGateway,
) {

    fun get(userId: String, nameFilters: List<String>): List<LeaguesForPlayer> {
        val notBestBallLeagues = GetOnlyStandardLeagues(leagueGateway).get(userId)
        val leaguesWithRosters = notBestBallLeagues.map {
            LeagueWithRosters(it, rosterGateway.findRostersOfLeague(it.id))
        }

        val players = playerGateway.getAllPlayers()
        return FilterPlayers(players, nameFilters).filterOutOfRosters(leaguesWithRosters)
    }
}
