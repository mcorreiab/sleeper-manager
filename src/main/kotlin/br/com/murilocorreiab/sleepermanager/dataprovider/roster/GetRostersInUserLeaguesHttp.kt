package br.com.murilocorreiab.sleepermanager.dataprovider.roster

import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.LeagueClient
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.UserClient
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueResponse
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.RosterClient
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.RosterResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import javax.inject.Singleton

@Singleton
class GetRostersInUserLeaguesHttp(
    private val userClient: UserClient,
    private val leagueClient: LeagueClient,
    private val rosterClient: RosterClient
) : GetRostersInUserLeagues {
    override suspend fun getAllRosters(username: String): Flow<Pair<LeagueResponse, Flow<RosterResponse>>> =
        userClient.getByUsername(username).awaitFirst()
            .let { leagueClient.getByUserId(it.userId).asFlow() }
            .map { getLeagueRosters(it) }

    private fun getLeagueRosters(league: LeagueResponse): Pair<LeagueResponse, Flow<RosterResponse>> =
        rosterClient.getRostersOfALeague(league.leagueId).asFlow()
            .let { Pair(league, it) }

    override suspend fun getUserRosters(username: String): Flow<Pair<LeagueResponse, Flow<RosterResponse>>> =
        userClient.getByUsername(username).awaitFirst()
            .let { user ->
                leagueClient.getByUserId(user.userId).asFlow().map { league -> filterUserRosters(league, user.userId) }
            }

    private fun filterUserRosters(league: LeagueResponse, userId: String): Pair<LeagueResponse, Flow<RosterResponse>> =
        rosterClient.getRostersOfALeague(league.leagueId).asFlow()
            .filter { roster -> roster.ownerId == userId }
            .let { Pair(league, it) }
}
