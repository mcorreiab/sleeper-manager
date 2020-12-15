package br.com.murilocorreiab.sleepermanager.dataprovider.roster

import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.LeagueClient
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.UserClient
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueResponse
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.RosterClient
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.RosterResponse
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import javax.inject.Singleton

@Singleton
class GetRostersInUserLeaguesHttp(
    private val userClient: UserClient,
    private val leagueClient: LeagueClient,
    private val rosterClient: RosterClient
) : GetRostersInUserLeagues {
    override suspend fun getAllRosters(username: String): List<Pair<LeagueResponse, List<RosterResponse>>> =
        userClient.getByUsername(username).awaitFirst()
            .let { leagueClient.getByUserId(it.userId).asFlow() }
            .map { getLeagueRosters(it) }.toList()

    private suspend fun getLeagueRosters(league: LeagueResponse): Pair<LeagueResponse, List<RosterResponse>> =
        rosterClient.getRostersOfALeague(league.leagueId).asFlow()
            .let { Pair(league, it.toList()) }

    override suspend fun getUserRosters(username: String): List<Pair<LeagueResponse, List<RosterResponse>>> =
        userClient.getByUsername(username).awaitFirst()
            .let { user ->
                leagueClient.getByUserId(user.userId).asFlow().map { league -> filterUserRosters(league, user.userId) }
                    .toList()
            }

    private suspend fun filterUserRosters(
        league: LeagueResponse,
        userId: String
    ): Pair<LeagueResponse, List<RosterResponse>> =
        rosterClient.getRostersOfALeague(league.leagueId).asFlow()
            .filter { roster -> roster.ownerId == userId }
            .let { Pair(league, it.toList()) }
}
