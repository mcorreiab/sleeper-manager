package br.com.murilocorreiab.sleepermanager.dataprovider.roster.entity

import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.RosterResponse
import br.com.murilocorreiab.sleepermanager.entities.league.Roster2
import br.com.murilocorreiab.sleepermanager.entities.player.RawPlayer

object RosterResponseFactory {
    fun build(
        starters: List<String> = listOf("0001"),
        players: List<String>? = listOf("0001"),
        ownerId: String = "ownerId",
        rosterId: String = "rosterId"
    ) = RosterResponse(rosterId, starters, players, ownerId)

    fun RosterResponse.toDomain() = Roster2(
        rosterId,
        ownerId,
        players?.map { RawPlayer(it) } ?: emptyList(),
        starters.map { RawPlayer(it) },
    )
}
