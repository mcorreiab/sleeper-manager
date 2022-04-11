package br.com.murilocorreiab.sleepermanager.dataprovider.roster

import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.RosterClient
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.RosterResponseMapper2
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster2
import br.com.murilocorreiab.sleepermanager.domain.roster.gateway.RosterGateway2
import jakarta.inject.Singleton
import org.mapstruct.factory.Mappers

@Singleton
class RosterGatewayImpl(private val rosterClient: RosterClient) : RosterGateway2 {

    private val rosterResponseMapper = Mappers.getMapper(RosterResponseMapper2::class.java)

    override fun findRostersOfLeague(id: String): List<Roster2> =
        rosterClient.getRostersOfALeague(id).map { rosterResponseMapper.toDomain(it) }
}
