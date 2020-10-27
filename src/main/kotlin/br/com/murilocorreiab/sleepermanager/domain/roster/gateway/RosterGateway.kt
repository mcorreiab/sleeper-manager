package br.com.murilocorreiab.sleepermanager.domain.roster.gateway

import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster
import kotlinx.coroutines.flow.Flow

interface RosterGateway {
    suspend fun findUserRostersInLeagues(username: String): Flow<Roster>
}
