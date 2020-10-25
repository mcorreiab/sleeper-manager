package br.com.murilocorreiab.sleepermanager.dataprovider.http.league.entity

data class LeagueResponseProducer(
    val name: String = "name",
    val leagueId: Long = 231,
    val totalRosters: Int = 12
) {
    fun build() = LeagueResponse(name, leagueId, totalRosters)
}
