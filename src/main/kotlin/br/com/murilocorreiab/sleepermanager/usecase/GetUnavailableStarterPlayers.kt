package br.com.murilocorreiab.sleepermanager.usecase

import br.com.murilocorreiab.sleepermanager.entities.league.RosterWithPlayers
import br.com.murilocorreiab.sleepermanager.entities.league.getPlayersOnRosters
import br.com.murilocorreiab.sleepermanager.entities.league.model.RosterUnavailablePlayers

class GetUnavailableStarterPlayers(
    private val leagueGateway: LeagueGateway,
    private val rosterGateway: RosterGateway,
    private val playerGateway: PlayerGateway,
) {

    fun get(userId: String): List<RosterUnavailablePlayers> {
        val notBestBallLeagues = GetOnlyStandardLeagues(leagueGateway).get(userId)
        val rosters = notBestBallLeagues.flatMap { rosterGateway.findRostersOfLeague(it.id) }
        val players = rosters.getPlayersOnRosters().mapNotNull { playerGateway.getById(it) }

        return RosterWithPlayers(rosters, players, notBestBallLeagues, userId).getUnavailableStarters()
    }
}
