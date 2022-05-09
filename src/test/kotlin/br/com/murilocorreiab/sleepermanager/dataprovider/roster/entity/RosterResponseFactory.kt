package br.com.murilocorreiab.sleepermanager.dataprovider.roster.entity

import br.com.murilocorreiab.sleepermanager.adapters.roster.RosterExternalResponse
import br.com.murilocorreiab.sleepermanager.entities.league.model.Roster

object RosterResponseFactory {
    fun build(
        starters: List<String> = listOf("0001"),
        players: List<String>? = listOf("0001"),
        ownerId: String = "ownerId",
        rosterId: String = "rosterId",
        leagueId: String = "leagueId",
    ) = RosterExternalResponse(rosterId, starters, players, ownerId, leagueId)

    fun RosterExternalResponse.toDomain() = Roster(
        rosterId,
        ownerId,
        players ?: emptyList(),
        starters,
        leagueId,
    )
}
