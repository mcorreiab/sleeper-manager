package br.com.murilocorreiab.sleepermanager.entities.league

import br.com.murilocorreiab.sleepermanager.entities.league.model.Roster2
import br.com.murilocorreiab.sleepermanager.entities.player.Player
import br.com.murilocorreiab.sleepermanager.entities.player.PlayerStatus
import br.com.murilocorreiab.sleepermanager.entities.player.RawPlayer

class RosterWithPlayers(private val rosters: List<Roster2>, private val players: List<Player>) {
    fun getUnavailableStarters(): List<Roster2> = rosters.associateBy(
        keySelector = { it },
        valueTransform = { mapNonActiveStarterPlayers(it) },
    ).filter { it.value.isNotEmpty() }.map { it.key.copy(players = it.value) }

    private fun mapNonActiveStarterPlayers(roster: Roster2) =
        roster.starters.mapNotNull { getPlayerWithId(it) }.filter { PlayerStatus.ACTIVE.status != it.injuryStatus }

    private fun getPlayerWithId(player: RawPlayer) = players.firstOrNull { player.id == it.id }
}
