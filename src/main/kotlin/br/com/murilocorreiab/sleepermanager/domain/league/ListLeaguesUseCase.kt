package br.com.murilocorreiab.sleepermanager.domain.league

import br.com.murilocorreiab.sleepermanager.domain.league.gateway.LeagueGateway
import javax.inject.Singleton

@Singleton
class ListLeaguesUseCase(private val leagueGateway: LeagueGateway) {
    suspend fun findUserLeagues(username: String) = leagueGateway.findUserLeagues(username)
}
