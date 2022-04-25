package br.com.murilocorreiab.sleepermanager.domain.roster.entity

import br.com.murilocorreiab.sleepermanager.domain.player.entity.Play
import br.com.murilocorreiab.sleepermanager.domain.player.entity.RawPlayer

object RosterFactory2 {
    fun build(
        players: List<Play> = arrayListOf(RawPlayer("1")),
        ownerId: String = "ownerId",
        id: String = "id",
        starters: List<String> = arrayListOf("1"),
    ) = Roster2(id = id, ownerId = ownerId, players = players, starters = starters.map { RawPlayer(it) })
}
