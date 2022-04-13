package br.com.murilocorreiab.sleepermanager.dataprovider.roster

import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.GetLeagues
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.UserClient
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueResponse
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.RosterClient
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.RosterResponse
import jakarta.inject.Singleton

@Singleton
class GetRostersInUserLeaguesHttp(
    private val userClient: UserClient,
    private val rosterClient: RosterClient,
    private val getLeagues: GetLeagues,
) : GetRostersInUserLeagues {

    override fun getUserRosters(username: String): List<Pair<LeagueResponse, List<RosterResponse>>> =
        userClient.getByUsername(username)
            .let { user ->
                getLeagues.getByUserId(user.userId).map { league -> filterUserRosters(league, user.userId) }
            }

    override fun getUserRostersById(userId: String): List<Pair<LeagueResponse, List<RosterResponse>>> =
        getLeagues.getByUserId(userId).map { filterUserRosters(it, userId) }.toList()

    private fun filterUserRosters(
        league: LeagueResponse,
        userId: String
    ): Pair<LeagueResponse, List<RosterResponse>> =
        rosterClient.getRostersOfALeague(league.leagueId)
            .filter { roster -> roster.ownerId == userId }
            .let { Pair(league, it.toList()) }
}
