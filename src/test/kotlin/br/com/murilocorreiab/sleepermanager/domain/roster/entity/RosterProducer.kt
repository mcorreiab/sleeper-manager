package br.com.murilocorreiab.sleepermanager.domain.roster.entity

data class RosterProducer(
    val players: List<Player> = arrayListOf(PlayerProducer().build()),
    val ownerId: String = "ownerId",
    val id: String = "id"
) {
    fun build() = Roster(id = id, ownerId = ownerId, players = players)
}
