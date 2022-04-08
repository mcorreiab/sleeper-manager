package br.com.murilocorreiab.sleepermanager.domain.roster.entity

object RosterFactory2 {
    fun build(
        players: List<String> = arrayListOf("1"),
        ownerId: String = "ownerId",
        id: String = "id",
    ) = Roster2(id = id, ownerId = ownerId, players = players)
}
