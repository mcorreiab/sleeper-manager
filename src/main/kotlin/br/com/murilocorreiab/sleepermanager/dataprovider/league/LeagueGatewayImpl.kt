package br.com.murilocorreiab.sleepermanager.dataprovider.league

import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.LeagueClient
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueMapper
import br.com.murilocorreiab.sleepermanager.domain.league.entity.League
import br.com.murilocorreiab.sleepermanager.domain.league.gateway.LeagueGateway
import jakarta.inject.Singleton
import org.mapstruct.factory.Mappers

@Singleton
class LeagueGatewayImpl(private val leagueClient: LeagueClient) : LeagueGateway {
    private val leagueMapper = Mappers.getMapper(LeagueMapper::class.java)

    override fun findAllUserLeagues(userId: String): List<League> =
        leagueClient.getByUserId(userId).map { leagueMapper.convertToDomain(it) }
}