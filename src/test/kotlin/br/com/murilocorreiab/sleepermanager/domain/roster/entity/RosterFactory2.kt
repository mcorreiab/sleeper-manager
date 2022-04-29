package br.com.murilocorreiab.sleepermanager.domain.roster.entity

import br.com.murilocorreiab.sleepermanager.entities.league.Roster2
import br.com.murilocorreiab.sleepermanager.entities.player.Play
import br.com.murilocorreiab.sleepermanager.entities.player.RawPlayer

object RosterFactory2 {
    fun build(
        players: List<Play> = arrayListOf(RawPlayer("1")),
        ownerId: String = "ownerId",
        id: String = "id",
        starters: List<String> = arrayListOf("1"),
    ) = Roster2(id = id, ownerId = ownerId, players = players, starters = starters.map { RawPlayer(it) })
}
