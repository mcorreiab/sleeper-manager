package br.com.murilocorreiab.sleepermanager.dataprovider.roster

import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.LeagueClient
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.UserClient
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueResponse
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.RosterClient
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.RosterResponse
import javax.inject.Singleton

@Singleton
class GetRostersInUserLeaguesHttp(
    private val userClient: UserClient,
    private val leagueClient: LeagueClient,
    private val rosterClient: RosterClient
) : GetRostersInUserLeagues {
    override fun getAllRosters(username: String): List<Pair<LeagueResponse, List<RosterResponse>>> =
        userClient.getByUsername(username)
            .let { leagueClient.getByUserId(it.userId) }
            .map { getLeagueRosters(it) }

    private fun getLeagueRosters(league: LeagueResponse): Pair<LeagueResponse, List<RosterResponse>> =
        Pair(league, rosterClient.getRostersOfALeague(league.leagueId))

    override fun getUserRosters(username: String): List<Pair<LeagueResponse, List<RosterResponse>>> =
        userClient.getByUsername(username)
            .let { user ->
                leagueClient.getByUserId(user.userId).map { league -> filterUserRosters(league, user.userId) }
            }

    override fun getUserRostersById(userId: String): List<Pair<LeagueResponse, List<RosterResponse>>> =
        leagueClient.getByUserId(userId).map { filterUserRosters(it, userId) }.toList()

    private fun filterUserRosters(
        league: LeagueResponse,
        userId: String
    ): Pair<LeagueResponse, List<RosterResponse>> =
        rosterClient.getRostersOfALeague(league.leagueId)
            .filter { roster -> roster.ownerId == userId }
            .let { Pair(league, it.toList()) }
}
