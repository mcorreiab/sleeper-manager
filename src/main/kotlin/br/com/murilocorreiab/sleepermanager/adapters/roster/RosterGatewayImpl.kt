package br.com.murilocorreiab.sleepermanager.adapters.roster

import br.com.murilocorreiab.sleepermanager.entities.league.model.Roster
import br.com.murilocorreiab.sleepermanager.usecase.RosterGateway2
import jakarta.inject.Singleton
import org.mapstruct.factory.Mappers

@Singleton
class RosterGatewayImpl(private val rosterClient: RosterClient) : RosterGateway2 {

    private val rosterResponseMapper = Mappers.getMapper(RosterExternalResponseMapper::class.java)

    override fun findRostersOfLeague(id: String): List<Roster> =
        rosterClient.getRostersOfALeague(id).map { rosterResponseMapper.toDomain(it) }
}
