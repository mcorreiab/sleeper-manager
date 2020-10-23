package br.com.murilocorreiab.sleepermanager.domain.usecase

import br.com.murilocorreiab.sleepermanager.domain.gateway.LeagueGateway
import javax.inject.Singleton

@Singleton
class ListLeaguesUseCase(private val leagueGateway: LeagueGateway) {
    fun findUserLeagues(username: String) = leagueGateway.findUserLeagues(username)
}
