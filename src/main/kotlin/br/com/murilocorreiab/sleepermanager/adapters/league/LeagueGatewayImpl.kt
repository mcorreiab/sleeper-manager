package br.com.murilocorreiab.sleepermanager.adapters.league

import br.com.murilocorreiab.sleepermanager.entities.league.model.League
import br.com.murilocorreiab.sleepermanager.usecase.LeagueGateway

class LeagueGatewayImpl(
    private val leagueClient: LeagueClient,
    private val leagueMapper: LeagueExternalResponseMapper,
) : LeagueGateway {

    override fun findAllUserLeagues(userId: String): List<League> =
        leagueClient.getByUserId(userId).map { leagueMapper.convertToDomain(it) }
}
