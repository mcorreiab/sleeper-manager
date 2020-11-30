package br.com.murilocorreiab.sleepermanager.entrypoint

import br.com.murilocorreiab.sleepermanager.domain.player.usecase.GetPlayersInWaiver
import br.com.murilocorreiab.sleepermanager.entrypoint.model.PlayersWaiverResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking

@Controller("/players")
class PlayerEntrypoint(private val getPlayersInWaiver: GetPlayersInWaiver) {

    @Get("/user/{username}/waiver{?players}")
    fun getPlayersInWaiverByLeague(
        @PathVariable username: String,
        @QueryValue players: String?
    ): Flow<PlayersWaiverResponse> = runBlocking {
        val playersToSearch = players?.split(",") ?: emptyList()
        getPlayersInWaiver.get(username, playersToSearch).map { (player, leagues) ->
            PlayersWaiverResponse(player, leagues.toList())
        }
    }
}
