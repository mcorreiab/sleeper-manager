package br.com.murilocorreiab.sleepermanager.entrypoint.client

import br.com.murilocorreiab.sleepermanager.entrypoint.model.PlayersWaiverResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

@Client("/players")
interface PlayerInWaiverClient {

    @Get("/user/{username}/waiver{?players}")
    fun getPlayersInWaiverByLeague(
        @PathVariable username: String,
        @QueryValue players: String?
    ): HttpResponse<List<PlayersWaiverResponse>>
}
