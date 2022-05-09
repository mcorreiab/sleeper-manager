package br.com.murilocorreiab.sleepermanager.adapters.league

data class LeagueExternalResponse(
    val name: String,
    val leagueId: String,
    val totalRosters: Int,
    val avatar: String?,
    val scoringSettings: ScoringSettingsResponse,
    val settings: SettingsResponse,
)
