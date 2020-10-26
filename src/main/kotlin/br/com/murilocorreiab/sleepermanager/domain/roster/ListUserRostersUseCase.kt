package br.com.murilocorreiab.sleepermanager.domain.roster

import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster
import br.com.murilocorreiab.sleepermanager.domain.roster.gateway.RosterGateway
import javax.inject.Singleton

@Singleton
class ListUserRostersUseCase(private val rosterGateway: RosterGateway) {
    suspend fun listUserRosterInLeague(userId: String, leagueId: String): Roster =
        rosterGateway.findUserRosterInLeague(userId, leagueId)
}
