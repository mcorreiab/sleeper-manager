package br.com.murilocorreiab.sleepermanager.framework

import br.com.murilocorreiab.sleepermanager.adapters.player.PlayerClient
import br.com.murilocorreiab.sleepermanager.adapters.player.PlayerExternalResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client

@Client("\${external.api.sleeper.player}")
interface PlayerApiClient : PlayerClient {

    @Get
    override fun getAllPlayers(): Map<String, PlayerExternalResponse>
}
