package br.com.murilocorreiab.sleepermanager.usecase

import br.com.murilocorreiab.sleepermanager.entities.league.model.Roster

interface RosterGateway {
    fun findRostersOfLeague(id: String): List<Roster>
}
