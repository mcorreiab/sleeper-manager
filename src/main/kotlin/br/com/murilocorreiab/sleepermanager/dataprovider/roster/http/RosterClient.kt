package br.com.murilocorreiab.sleepermanager.dataprovider.roster.http

import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.RosterResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.client.annotation.Client

@Client
interface RosterClient {

    @Get("\${external.api.sleeper.roster}")
    fun getRostersOfALeague(@PathVariable leagueId: String): List<RosterResponse>
}
