package br.com.murilocorreiab.sleepermanager.dataprovider.league.http

import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.UserResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.client.annotation.Client

@Client("\${external.api.sleeper.user.root}")
interface UserClient {

    @Get("\${external.api.sleeper.user.getByUsername}")
    fun getByUsername(@PathVariable username: String): UserResponse
}
