package br.com.murilocorreiab.sleepermanager.entities.league

import br.com.murilocorreiab.sleepermanager.entities.league.model.League
import br.com.murilocorreiab.sleepermanager.entities.league.model.Roster2
import br.com.murilocorreiab.sleepermanager.entities.league.model.RosterUnavailablePlayers
import br.com.murilocorreiab.sleepermanager.entities.player.Player
import br.com.murilocorreiab.sleepermanager.entities.player.PlayerStatus

class RosterWithPlayers(
    private val rosters: List<Roster2>,
    private val players: List<Player>,
    private val leagues: List<League>,
    private val userId: String,
) {
    fun getUnavailableStarters(): List<RosterUnavailablePlayers> = rosters.filterRostersOfUser().associateBy(
        keySelector = { it },
        valueTransform = { mapNonActiveStarterPlayers(it) },
    ).filter { it.value.isNotEmpty() }
        .map { RosterUnavailablePlayers(it.key.id, it.key.ownerId, it.value, getLeague(it.key.leagueId)) }

    private fun List<Roster2>.filterRostersOfUser() = filter { it.ownerId == userId }

    private fun mapNonActiveStarterPlayers(roster: Roster2) =
        roster.starters.mapNotNull { getPlayerWithId(it) }.filter { PlayerStatus.ACTIVE.status != it.injuryStatus }

    private fun getPlayerWithId(player: String) = players.firstOrNull { player == it.id }

    private fun getLeague(leagueId: String) = leagues.first { leagueId == it.id }
}
