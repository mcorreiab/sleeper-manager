package br.com.murilocorreiab.sleepermanager.entities.league.model

object RosterFactory {
    fun build(
        players: List<String> = arrayListOf("1"),
        ownerId: String = "ownerId",
        id: String = "id",
        starters: List<String> = arrayListOf("1"),
        leagueId: String = "1",
    ) = Roster(id = id, ownerId = ownerId, players = players, starters = starters, leagueId = leagueId)
}
