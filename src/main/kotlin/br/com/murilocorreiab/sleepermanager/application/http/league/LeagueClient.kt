package br.com.murilocorreiab.sleepermanager.application.http.league

import br.com.murilocorreiab.sleepermanager.application.http.league.entity.LeagueResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.client.annotation.Client
import io.reactivex.Flowable

@Client("\${external.api.sleeper.league.root}")
interface LeagueClient {

    @Get("\${external.api.sleeper.league.getLeagues}")
    fun getByUserId(@PathVariable userId: Long): Flowable<LeagueResponse>
}
