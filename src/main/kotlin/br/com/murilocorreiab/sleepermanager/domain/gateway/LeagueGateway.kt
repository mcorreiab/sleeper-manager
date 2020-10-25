package br.com.murilocorreiab.sleepermanager.domain.gateway

import br.com.murilocorreiab.sleepermanager.domain.entity.League
import kotlinx.coroutines.flow.Flow

interface LeagueGateway {
    suspend fun findUserLeagues(username: String): Flow<League>
}
