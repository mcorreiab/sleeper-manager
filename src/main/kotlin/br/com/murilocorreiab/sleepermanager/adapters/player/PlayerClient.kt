package br.com.murilocorreiab.sleepermanager.adapters.player

import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client

@Client("\${external.api.sleeper.player}")
interface PlayerClient {

    @Get
    fun getAllPlayers(): Map<String, PlayerExternalResponse>
}
