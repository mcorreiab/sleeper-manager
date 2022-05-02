package br.com.murilocorreiab.sleepermanager.usecase

import br.com.murilocorreiab.sleepermanager.entities.league.model.League

interface LeagueGateway {
    fun findAllUserLeagues(userId: String): List<League>
}
