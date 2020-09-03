package br.com.murilocorreiab.sleepermanager.domain.gateway

import br.com.murilocorreiab.sleepermanager.domain.entity.League

interface LeagueGateway {
    fun findUserLeagues(username: String): List<League>
}