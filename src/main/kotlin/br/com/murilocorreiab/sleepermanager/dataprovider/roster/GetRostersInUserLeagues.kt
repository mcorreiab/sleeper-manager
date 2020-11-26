package br.com.murilocorreiab.sleepermanager.dataprovider.roster

import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueResponse
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.RosterResponse
import kotlinx.coroutines.flow.Flow

interface GetRostersInUserLeagues {
    suspend fun getAllRosters(username: String): Flow<Pair<LeagueResponse, Flow<RosterResponse>>>
    suspend fun getUserRosters(username: String): Flow<Pair<LeagueResponse, Flow<RosterResponse>>>
}
