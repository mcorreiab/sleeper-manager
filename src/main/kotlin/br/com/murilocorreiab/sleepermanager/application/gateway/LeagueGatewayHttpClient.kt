package br.com.murilocorreiab.sleepermanager.application.gateway

import br.com.murilocorreiab.sleepermanager.application.http.league.LeagueClient
import br.com.murilocorreiab.sleepermanager.application.http.league.entity.LeagueMapper
import br.com.murilocorreiab.sleepermanager.application.http.user.UserClient
import br.com.murilocorreiab.sleepermanager.domain.entity.League
import br.com.murilocorreiab.sleepermanager.domain.gateway.LeagueGateway
import javax.inject.Singleton

@Singleton
class LeagueGatewayHttpClient(private val userClient: UserClient,
                              private val leagueClient: LeagueClient,
                              private val leagueMapper: LeagueMapper) : LeagueGateway {
    override fun findUserLeagues(username: String): List<League> {
        return userClient.getByUsername(username).flatMapPublisher {
            leagueClient.getByUserId(it.userId)
        }.map {
            leagueMapper.convertToDomain(it)
        }.blockingIterable().toList()
    }
}
