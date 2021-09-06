package br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity

object LeagueResponseProducer {
    fun build(
        name: String = "name",
        leagueId: String = "231",
        totalRosters: Int = 12,
        avatar: String = "avatar",
        scoringSettingsResponse: ScoringSettingsResponse = ScoringSettingsResponseProducer.build(),
        settings: SettingsResponse = SettingsResponseProducer.build()
    ) = LeagueResponse(name, leagueId, totalRosters, avatar, scoringSettingsResponse, settings)
}
