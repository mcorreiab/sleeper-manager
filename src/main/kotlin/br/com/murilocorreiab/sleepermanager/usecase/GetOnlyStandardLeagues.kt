package br.com.murilocorreiab.sleepermanager.usecase

import br.com.murilocorreiab.sleepermanager.entities.league.filterOutBestBallLeagues

class GetOnlyStandardLeagues(private val leagueGateway: LeagueGateway) {

    fun get(userId: String) = leagueGateway.findAllUserLeagues(userId).filterOutBestBallLeagues()
}
