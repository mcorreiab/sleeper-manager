package br.com.murilocorreiab.sleepermanager.domain.roster.gateway

import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster2

interface RosterGateway2 {
    fun findRostersOfLeague(id: String): List<Roster2>
}
