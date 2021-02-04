package br.com.murilocorreiab.sleepermanager.domain.league.entity

data class LeagueProducer(
    val name: String = "league",
    val id: String = "120",
    val size: Int = 10,
    val avatar: String = "avatar",
    val pointsByReception: PointsByReception = PointsByReception.STANDARD
) {
    fun build() = League(name = name, id = id, size = size, avatar = avatar, pointsByReception = pointsByReception)
}
