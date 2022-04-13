package br.com.murilocorreiab.sleepermanager.dataprovider.roster

import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueResponse
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.RosterResponse

interface GetRostersInUserLeagues {
    fun getUserRosters(username: String): List<Pair<LeagueResponse, List<RosterResponse>>>
    fun getUserRostersById(userId: String): List<Pair<LeagueResponse, List<RosterResponse>>>
}
