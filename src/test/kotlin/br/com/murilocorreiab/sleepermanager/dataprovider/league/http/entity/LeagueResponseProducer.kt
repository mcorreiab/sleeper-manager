package br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity

import br.com.murilocorreiab.sleepermanager.domain.league.entity.League
import br.com.murilocorreiab.sleepermanager.domain.league.entity.PointsByReception

object LeagueResponseProducer {
    fun build(
        name: String = "name",
        leagueId: String = "231",
        totalRosters: Int = 12,
        avatar: String = "avatar",
        scoringSettingsResponse: ScoringSettingsResponse = ScoringSettingsResponseProducer.build(),
        settings: SettingsResponse = SettingsResponseProducer.build()
    ) = LeagueResponse(name, leagueId, totalRosters, avatar, scoringSettingsResponse, settings)

    fun toDomain(leagueResponse: LeagueResponse, pointsByReception: PointsByReception) = League(
        leagueResponse.name,
        leagueResponse.leagueId,
        leagueResponse.totalRosters,
        leagueResponse.avatar,
        pointsByReception,
    )
}
