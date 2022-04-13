package br.com.murilocorreiab.sleepermanager.domain.roster.gateway

import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster

interface RosterGateway {
    fun findUserRostersByUsernameInLeagues(username: String): List<Roster>
    fun findUserRostersByUserIdInLeagues(userId: String): List<Roster>
}
