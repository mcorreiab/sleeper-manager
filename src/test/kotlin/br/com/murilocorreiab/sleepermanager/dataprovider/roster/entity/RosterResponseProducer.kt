package br.com.murilocorreiab.sleepermanager.dataprovider.roster.entity

import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.RosterResponse

data class RosterResponseProducer(
    val starters: List<String> = listOf("0001"),
    val players: List<String> = listOf("0001"),
    val ownerId: String = "ownerId",
    val rosterId: String = "rosterId"
) {
    fun build() = RosterResponse(rosterId, starters, players, ownerId)
}
