package br.com.murilocorreiab.sleepermanager.dataprovider.roster.http

import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.PlayerResponse
import io.micronaut.http.client.annotation.Client
import kotlinx.coroutines.flow.Flow

@Client("\${external.api.sleeper.player}")
interface PlayerClient {
    fun getAllPlayers(): Flow<PlayerResponse>
}
