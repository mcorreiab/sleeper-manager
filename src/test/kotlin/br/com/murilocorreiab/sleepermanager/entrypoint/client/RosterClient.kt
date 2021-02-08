package br.com.murilocorreiab.sleepermanager.entrypoint.client

import br.com.murilocorreiab.sleepermanager.entrypoint.entity.RosterResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.client.annotation.Client

@Client("/rosters")
interface RosterClient {

    @Get("/user/username/{username}/unavailable")
    fun recoverRosterOfAUserByUsername(@PathVariable username: String): HttpResponse<List<RosterResponse>>

    @Get("/user/userId/{userId}/unavailable")
    fun recoverRosterOfAUserById(@PathVariable userId: String): HttpResponse<List<RosterResponse>>
}
