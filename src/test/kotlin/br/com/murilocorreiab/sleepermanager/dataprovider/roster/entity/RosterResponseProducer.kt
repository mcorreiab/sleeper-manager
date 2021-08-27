package br.com.murilocorreiab.sleepermanager.dataprovider.roster.entity

import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.RosterResponse

object RosterResponseProducer {
    fun build(
        starters: List<String> = listOf("0001"),
        players: List<String>? = listOf("0001"),
        ownerId: String = "ownerId",
        rosterId: String = "rosterId"
    ) = RosterResponse(rosterId, starters, players, ownerId)
}
