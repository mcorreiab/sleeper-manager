package br.com.murilocorreiab.sleepermanager.adapters.roster

import br.com.murilocorreiab.sleepermanager.entities.league.model.Roster
import br.com.murilocorreiab.sleepermanager.usecase.RosterGateway

class RosterGatewayImpl(
    private val rosterClient: RosterClient,
    private val rosterResponseMapper: RosterExternalResponseMapper,
) : RosterGateway {

    override fun findRostersOfLeague(id: String): List<Roster> =
        rosterClient.getRostersOfALeague(id).map { rosterResponseMapper.toDomain(it) }
}
