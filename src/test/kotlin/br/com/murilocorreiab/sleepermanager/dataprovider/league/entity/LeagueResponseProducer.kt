package br.com.murilocorreiab.sleepermanager.dataprovider.league.entity

import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueResponse

data class LeagueResponseProducer(
    val name: String = "name",
    val leagueId: String = "231",
    val totalRosters: Int = 12
) {
    fun build() = LeagueResponse(name, leagueId, totalRosters)
}
