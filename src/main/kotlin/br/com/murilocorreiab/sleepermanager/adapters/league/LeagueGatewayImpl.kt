package br.com.murilocorreiab.sleepermanager.adapters.league

import br.com.murilocorreiab.sleepermanager.entities.league.model.League
import br.com.murilocorreiab.sleepermanager.usecase.LeagueGateway
import org.mapstruct.factory.Mappers

class LeagueGatewayImpl(private val leagueClient: LeagueClient) : LeagueGateway {
    private val leagueMapper = Mappers.getMapper(LeagueExternalResponseMapper::class.java)

    override fun findAllUserLeagues(userId: String): List<League> =
        leagueClient.getByUserId(userId).map { leagueMapper.convertToDomain(it) }
}
