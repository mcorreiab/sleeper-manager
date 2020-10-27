package br.com.murilocorreiab.sleepermanager.dataprovider.roster.http

import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.RosterResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.client.annotation.Client
import kotlinx.coroutines.flow.Flow

@Client("\${external.api.sleeper.user.roster}")
interface RosterClient {

    @Get
    fun getRostersOfALeague(@PathVariable leagueId: String): Flow<RosterResponse>
}
