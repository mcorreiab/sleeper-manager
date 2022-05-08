package br.com.murilocorreiab.sleepermanager.dataprovider.roster.entity

import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.RosterResponse
import br.com.murilocorreiab.sleepermanager.entities.league.model.Roster2

object RosterResponseFactory {
    fun build(
        starters: List<String> = listOf("0001"),
        players: List<String>? = listOf("0001"),
        ownerId: String = "ownerId",
        rosterId: String = "rosterId",
        leagueId: String = "leagueId",
    ) = RosterResponse(rosterId, starters, players, ownerId, leagueId)

    fun RosterResponse.toDomain() = Roster2(
        rosterId,
        ownerId,
        players ?: emptyList(),
        starters,
        leagueId,
    )
}
