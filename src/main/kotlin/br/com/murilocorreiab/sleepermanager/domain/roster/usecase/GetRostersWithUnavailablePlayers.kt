package br.com.murilocorreiab.sleepermanager.domain.roster.usecase

import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster

interface GetRostersWithUnavailablePlayers {
    suspend fun get(username: String): List<Roster>
}
