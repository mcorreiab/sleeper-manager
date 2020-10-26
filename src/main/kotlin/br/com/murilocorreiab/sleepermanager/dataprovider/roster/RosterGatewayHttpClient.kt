package br.com.murilocorreiab.sleepermanager.dataprovider.roster

import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster
import br.com.murilocorreiab.sleepermanager.domain.roster.gateway.RosterGateway
import javax.inject.Singleton

@Singleton
class RosterGatewayHttpClient : RosterGateway {
    override suspend fun findUserRosterInLeague(userId: String, leagueId: String): Roster {
        TODO("Not yet implemented")
    }
}
