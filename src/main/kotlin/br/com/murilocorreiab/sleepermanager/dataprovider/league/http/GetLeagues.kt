package br.com.murilocorreiab.sleepermanager.dataprovider.league.http

import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueResponse
import jakarta.inject.Singleton

@Singleton
class GetLeagues(private val leagueClient: LeagueClient) {
    fun getByUserId(userId: String): List<LeagueResponse> =
        leagueClient.getByUserId(userId).filter { it.settings.bestBall != 1 }
}
