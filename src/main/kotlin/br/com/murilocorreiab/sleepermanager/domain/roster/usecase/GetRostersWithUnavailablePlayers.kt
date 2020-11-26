package br.com.murilocorreiab.sleepermanager.domain.roster.usecase

import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster
import kotlinx.coroutines.flow.Flow

interface GetRostersWithUnavailablePlayers {
    suspend fun get(username: String): Flow<Roster>
}
