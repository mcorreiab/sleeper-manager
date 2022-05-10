package br.com.murilocorreiab.sleepermanager.adapters.league

interface LeagueClient {
    fun getByUserId(userId: String): List<LeagueExternalResponse>
}
