package br.com.murilocorreiab.sleepermanager.dataprovider.roster.entity

import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.RosterResponse
import br.com.murilocorreiab.sleepermanager.domain.player.entity.RawPlayer
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster2

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
