package br.com.murilocorreiab.sleepermanager.dataprovider.league.entity

import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueResponse
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.ScoringSettingsResponse

data class LeagueResponseProducer(
    val name: String = "name",
    val leagueId: String = "231",
    val totalRosters: Int = 12,
    val avatar: String = "avatar",
    val scoringSettingsResponse: ScoringSettingsResponse = ScoringSettingsResponseProducer().build()
) {
    fun build() = LeagueResponse(name, leagueId, totalRosters, avatar, scoringSettingsResponse)
}
