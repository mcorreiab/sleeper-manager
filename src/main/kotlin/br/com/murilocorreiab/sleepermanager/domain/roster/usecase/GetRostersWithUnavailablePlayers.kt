package br.com.murilocorreiab.sleepermanager.domain.roster.usecase

import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster

interface GetRostersWithUnavailablePlayers {
    fun getByUsername(username: String): List<Roster>
    fun getByUserId(userId: String): List<Roster>
}
