package br.com.murilocorreiab.sleepermanager.domain.league.gateway

import br.com.murilocorreiab.sleepermanager.domain.league.entity.League

interface LeagueGateway {
    fun findAllUserLeagues(userId: String): List<League>
}
