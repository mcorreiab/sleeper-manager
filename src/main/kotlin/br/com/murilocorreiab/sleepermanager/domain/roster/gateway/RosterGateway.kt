package br.com.murilocorreiab.sleepermanager.domain.roster.gateway

import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster

interface RosterGateway {
    suspend fun findUserRosterInLeague(userId: String, leagueId: String): Roster
}
