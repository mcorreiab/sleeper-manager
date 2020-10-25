package br.com.murilocorreiab.sleepermanager.dataprovider.http.league

import br.com.murilocorreiab.sleepermanager.dataprovider.http.league.entity.LeagueMapper
import br.com.murilocorreiab.sleepermanager.dataprovider.http.user.UserClient
import br.com.murilocorreiab.sleepermanager.domain.entity.League
import br.com.murilocorreiab.sleepermanager.domain.gateway.LeagueGateway
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mapstruct.factory.Mappers
import javax.inject.Singleton

@Singleton
class LeagueGatewayHttpClient(
    private val userClient: UserClient,
    private val leagueClient: LeagueClient
) : LeagueGateway {

    private val leagueMapper = Mappers.getMapper(LeagueMapper::class.java)

    override suspend fun findUserLeagues(username: String): Flow<League> =
        userClient.getByUsername(username).let {
            leagueClient.getByUserId(it.userId)
        }.map {
            leagueMapper.convertToDomain(it)
        }
}
