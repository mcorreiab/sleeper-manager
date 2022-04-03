package br.com.murilocorreiab.sleepermanager.domain.player.usecase

import br.com.murilocorreiab.sleepermanager.domain.league.entity.League
import br.com.murilocorreiab.sleepermanager.domain.league.gateway.LeagueGateway
import br.com.murilocorreiab.sleepermanager.domain.player.entity.LeaguesForPlayer
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import br.com.murilocorreiab.sleepermanager.domain.player.gateway.PlayerGateway
import br.com.murilocorreiab.sleepermanager.domain.roster.gateway.RosterGateway2
import javax.inject.Singleton

@Singleton
class PlayersInWaiverUseCase(
    private val leagueGateway: LeagueGateway,
    private val rosterGateway: RosterGateway2,
    private val playerGateway: PlayerGateway,
) {
    fun checkIfPlayersAreInWaiver(userId: String, filter: List<String>): List<LeaguesForPlayer> {
        val leaguesWithPlayers = getPlayersOfUserLeagues(userId)

        val players = playerGateway.getAllPlayers().filterWithNameMatching(filter)

        val playersInWaiver = PlayersInWaiver()

        leaguesWithPlayers.forEach { (league, playersInRosters) ->
            players.filterOutOfRosters(playersInRosters).forEach {
                playersInWaiver.add(it, league)
            }
        }

        return playersInWaiver.getLeaguesForEachPlayer()
    }

    private fun getPlayersOfUserLeagues(userId: String) =
        leagueGateway.findAllUserLeagues(userId).map { it.collectLeaguePlayers() }

    private fun League.collectLeaguePlayers() =
        rosterGateway.findRostersOfLeague(this.id).flatMap { it.players }.let { LeagueWithPlayers(this, it) }

    private fun List<Player>.filterWithNameMatching(filter: List<String>) =
        this.filter { playerNameMatches(filter, it.name) }

    private fun playerNameMatches(filter: List<String>, name: String) =
        filter.any { name.contains(it, ignoreCase = true) }

    private fun List<Player>.filterOutOfRosters(
        rosteredPlayers: List<String>
    ) = this.filter { isPlayerRostered(rosteredPlayers, it) }

    private fun isPlayerRostered(
        rosteredPlayers: List<String>,
        player: Player
    ) = !rosteredPlayers.any { it == player.id }

    private data class LeagueWithPlayers(val league: League, val players: List<String>)

    private class PlayersInWaiver {
        val leaguesByPlayer: MutableMap<Player, MutableList<League>> = mutableMapOf()

        fun add(player: Player, league: League) {
            leaguesByPlayer[player]?.add(league) ?: leaguesByPlayer.put(player, mutableListOf(league))
        }

        fun getLeaguesForEachPlayer() = leaguesByPlayer.map { LeaguesForPlayer(it.key, it.value) }
    }
}
