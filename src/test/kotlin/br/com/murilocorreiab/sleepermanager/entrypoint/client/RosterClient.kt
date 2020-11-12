package br.com.murilocorreiab.sleepermanager.entrypoint.client

import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.client.annotation.Client

@Client("/rosters")
interface RosterClient {

    @Get("/user/{username}/unavailable")
    fun recoverRosterOfAUser(@PathVariable username: String): HttpResponse<List<Roster>>
}
