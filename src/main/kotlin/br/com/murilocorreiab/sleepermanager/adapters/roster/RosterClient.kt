package br.com.murilocorreiab.sleepermanager.adapters.roster

import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.client.annotation.Client

@Client
interface RosterClient {

    @Get("\${external.api.sleeper.roster}")
    fun getRostersOfALeague(@PathVariable leagueId: String): List<RosterExternalResponse>
}
