package br.com.murilocorreiab.sleepermanager.framework

import br.com.murilocorreiab.sleepermanager.adapters.roster.RosterClient
import br.com.murilocorreiab.sleepermanager.adapters.roster.RosterExternalResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.client.annotation.Client

@Client
interface RosterApiClient : RosterClient {

    @Get("\${external.api.sleeper.roster}")
    override fun getRostersOfALeague(@PathVariable leagueId: String): List<RosterExternalResponse>
}
