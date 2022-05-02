package br.com.murilocorreiab.sleepermanager.entities.league

import br.com.murilocorreiab.sleepermanager.entities.league.model.League
import br.com.murilocorreiab.sleepermanager.entities.league.model.LeaguesForPlayer
import br.com.murilocorreiab.sleepermanager.entities.league.model.Roster2
import br.com.murilocorreiab.sleepermanager.entities.player.Player
import br.com.murilocorreiab.sleepermanager.entities.player.RawPlayer

data class LeagueWithRosters(val league: League, val rosters: List<Roster2>)

fun List<Player>.getOutOfRosters(leaguesWithRosters: List<LeagueWithRosters>): List<LeaguesForPlayer> =
    this.mapNotNull {
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
