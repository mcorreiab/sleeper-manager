package br.com.murilocorreiab.sleepermanager.domain.usecase

import br.com.murilocorreiab.sleepermanager.domain.gateway.LeagueGateway
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListLeaguesUseCase @Inject constructor(private val leagueGateway: LeagueGateway) {
    fun findUserLeagues(username: String) = leagueGateway.findUserLeagues(username)

}
