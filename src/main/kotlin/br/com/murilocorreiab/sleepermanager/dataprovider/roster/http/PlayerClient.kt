package br.com.murilocorreiab.sleepermanager.dataprovider.roster.http

import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.PlayerResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client

@Client("\${external.api.sleeper.player}")
interface PlayerClient {

    @Get
    fun getAllPlayers(): Map<String, PlayerResponse>
}
