package br.com.murilocorreiab.sleepermanager.framework

import br.com.murilocorreiab.sleepermanager.adapters.league.LeagueClient
import br.com.murilocorreiab.sleepermanager.adapters.league.LeagueExternalResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.client.annotation.Client

@Client("\${external.api.sleeper.user.root}")
interface LeagueApiClient : LeagueClient {

    @Get("\${external.api.sleeper.user.getLeagues}")
    override fun getByUserId(@PathVariable userId: String): List<LeagueExternalResponse>
}
