package br.com.murilocorreiab.sleepermanager.dataprovider.league.http

import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.client.annotation.Client
import kotlinx.coroutines.flow.Flow

@Client("\${external.api.sleeper.league.root}")
interface LeagueClient {

    @Get("\${external.api.sleeper.league.getLeagues}")
    fun getByUserId(@PathVariable userId: Long): Flow<LeagueResponse>
}
