package br.com.murilocorreiab.sleepermanager.entities.player

import br.com.murilocorreiab.sleepermanager.entities.league.LeagueWithRosters
import br.com.murilocorreiab.sleepermanager.entities.league.LeaguesForPlayer

class Players(private val players: List<Player>) {
    fun filterByName(nameFilters: List<String>): List<Player> = if (nameFilters.isEmpty()) {
        players
    } else {
        filterPlayerWithNameMatchesAnyFilter(nameFilters)
    }

    private fun filterPlayerWithNameMatchesAnyFilter(nameFilters: List<String>) = players.filter {
        it.playerNameMatchesAnyFilter(
            nameFilters,
        )
    }

    private fun Player.playerNameMatchesAnyFilter(nameFilters: List<String>) =
        nameFilters.any { this.name.contains(it, true) }

    fun getOutOfRosters(leaguesWithRosters: List<LeagueWithRosters>): List<LeaguesForPlayer> = players.mapNotNull {
        val leagues = leaguesWithRosters.getLeaguesWithPlayerAvailable(it)

        if (leagues.isNotEmpty()) {
            LeaguesForPlayer(it, leagues)
        } else {
            null
        }
    }

    private fun List<LeagueWithRosters>.getLeaguesWithPlayerAvailable(player: Player) = this.mapNotNull {
        if (it.isPlayerOutOfAllRosters(player)) {
            it.league
        } else {
            null
        }
    }

    private fun LeagueWithRosters.isPlayerOutOfAllRosters(player: Player) =
        !this.rosters.flatMap { it.players }.contains(RawPlayer(player.id))
}
