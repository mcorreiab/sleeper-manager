package br.com.murilocorreiab.sleepermanager.usecase

import br.com.murilocorreiab.sleepermanager.entities.league.Roster2

interface RosterGateway2 {
    fun findRostersOfLeague(id: String): List<Roster2>
}
