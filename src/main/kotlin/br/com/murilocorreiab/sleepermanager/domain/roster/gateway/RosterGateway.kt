package br.com.murilocorreiab.sleepermanager.domain.roster.gateway

import br.com.murilocorreiab.sleepermanager.domain.league.entity.League
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster
import kotlinx.coroutines.flow.Flow

interface RosterGateway {
    suspend fun findUserRostersInLeagues(username: String): Flow<Roster>
    suspend fun findAllRosteredPlayersInUserLeagues(username: String): Flow<Pair<League, Flow<Player>>>
}
