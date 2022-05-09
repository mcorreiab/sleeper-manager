package br.com.murilocorreiab.sleepermanager.framework

import br.com.murilocorreiab.sleepermanager.adapters.league.LeagueExternalResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.client.annotation.Client

@Client("\${external.api.sleeper.user.root}")
interface LeagueClient {

    @Get("\${external.api.sleeper.user.getLeagues}")
    fun getByUserId(@PathVariable userId: String): List<LeagueExternalResponse>
}
