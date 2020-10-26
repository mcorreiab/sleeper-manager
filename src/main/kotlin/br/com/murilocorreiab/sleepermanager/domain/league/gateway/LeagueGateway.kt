package br.com.murilocorreiab.sleepermanager.domain.league.gateway

import br.com.murilocorreiab.sleepermanager.domain.league.entity.League
import kotlinx.coroutines.flow.Flow

interface LeagueGateway {
    suspend fun findUserLeagues(username: String): Flow<League>
}
