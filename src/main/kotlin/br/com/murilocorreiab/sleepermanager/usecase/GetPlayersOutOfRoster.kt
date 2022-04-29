package br.com.murilocorreiab.sleepermanager.usecase

import br.com.murilocorreiab.sleepermanager.entities.league.LeagueWithRosters
import br.com.murilocorreiab.sleepermanager.entities.league.Leagues
import br.com.murilocorreiab.sleepermanager.entities.league.LeaguesForPlayer
import br.com.murilocorreiab.sleepermanager.entities.player.Players
import jakarta.inject.Singleton

@Singleton
class GetPlayersOutOfRoster(
    private val leagueGateway: LeagueGateway,
    private val playerGateway: PlayerGateway,
    private val rosterGateway: RosterGateway2,
) {

    fun get(userId: String, nameFilters: List<String>): List<LeaguesForPlayer> {
        val userLeagues = leagueGateway.findAllUserLeagues(userId)
        val notBestBallLeagues = Leagues(userLeagues).filterOutBestBallLeagues()
        val leaguesWithRosters = notBestBallLeagues.map {
            LeagueWithRosters(it, rosterGateway.findRostersOfLeague(it.id))
        }

        val players = playerGateway.getAllPlayers()

        val playersMatchFilter = Players(players).filterByName(nameFilters)
        return Players(playersMatchFilter).getOutOfRosters(leaguesWithRosters)
    }
}
