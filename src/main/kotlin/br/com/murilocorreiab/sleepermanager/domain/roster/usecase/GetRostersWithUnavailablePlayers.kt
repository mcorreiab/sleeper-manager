package br.com.murilocorreiab.sleepermanager.domain.roster.usecase

import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster

interface GetRostersWithUnavailablePlayers {
    suspend fun getByUsername(username: String): List<Roster>
    suspend fun getByUserId(userId: String): List<Roster>
}
