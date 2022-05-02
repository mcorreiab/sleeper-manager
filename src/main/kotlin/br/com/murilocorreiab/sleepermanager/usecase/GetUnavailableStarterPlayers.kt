package br.com.murilocorreiab.sleepermanager.usecase

import br.com.murilocorreiab.sleepermanager.entities.league.RosterWithPlayers
import br.com.murilocorreiab.sleepermanager.entities.league.model.Roster2

class GetUnavailableStarterPlayers(
    private val leagueGateway: LeagueGateway,
    private val rosterGateway: RosterGateway2,
    private val playerGateway: PlayerGateway,
) {

    fun get(userId: String): List<Roster2> {
        val notBestBallLeagues = GetOnlyStandardLeagues(leagueGateway).get(userId)
        val rosters = notBestBallLeagues.flatMap { rosterGateway.findRostersOfLeague(it.id) }
        val players = playerGateway.getAllPlayers()

        return RosterWithPlayers(rosters, players).getUnavailableStarters()
    }
}
