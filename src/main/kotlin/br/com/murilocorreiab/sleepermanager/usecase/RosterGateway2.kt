package br.com.murilocorreiab.sleepermanager.usecase

import br.com.murilocorreiab.sleepermanager.entities.league.model.Roster

interface RosterGateway2 {
    fun findRostersOfLeague(id: String): List<Roster>
}
