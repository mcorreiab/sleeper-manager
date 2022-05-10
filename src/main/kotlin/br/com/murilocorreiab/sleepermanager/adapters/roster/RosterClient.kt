package br.com.murilocorreiab.sleepermanager.adapters.roster

interface RosterClient {
    fun getRostersOfALeague(leagueId: String): List<RosterExternalResponse>
}
