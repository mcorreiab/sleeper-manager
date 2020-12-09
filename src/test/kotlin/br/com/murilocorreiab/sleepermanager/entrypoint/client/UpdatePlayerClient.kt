package br.com.murilocorreiab.sleepermanager.entrypoint.client

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client

@Client("/players")
interface UpdatePlayerClient {

    @Post("/update")
    fun update(): HttpResponse<Unit>
}
