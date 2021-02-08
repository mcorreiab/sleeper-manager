package br.com.murilocorreiab.sleepermanager.entrypoint

import br.com.murilocorreiab.sleepermanager.domain.player.usecase.GetPlayersInWaiver
import br.com.murilocorreiab.sleepermanager.entrypoint.entity.PlayersWaiverResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpResponse.badRequest
import io.micronaut.http.HttpResponse.notFound
import io.micronaut.http.HttpResponse.ok
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import kotlinx.coroutines.runBlocking

@Controller("/players")
class PlayerInWaiverEntrypoint(private val getPlayersInWaiver: GetPlayersInWaiver) {

    @Operation(
        summary = "Get Players in waiver",
        description = "Get all leagues in which at least one of a list of players is available"
    )
    @ApiResponses(
        ApiResponse(
            description = "One or more players are available in one or more league's waivers",
            content = [Content(array = ArraySchema(schema = Schema(implementation = PlayersWaiverResponse::class)))]
        ),
        ApiResponse(responseCode = "400", description = "Should inform one or more players"),
        ApiResponse(responseCode = "404", description = "Couldn't find any of the informed players in waivers")
    )
    @Get("/user/{username}/waiver{?players}")
    fun getPlayersInWaiverByLeague(
        @PathVariable username: String,
        @QueryValue players: String?
    ): HttpResponse<List<PlayersWaiverResponse>> = runBlocking {
        players?.let {
            val namesToSearch = getNamesToSearch(it)
            if (namesToSearch.isNotEmpty()) {
                getPlayers(username, namesToSearch)
            } else {
                badRequest()
            }
        } ?: badRequest()
    }

    private fun getNamesToSearch(playersQuery: String): List<String> =
        playersQuery.split(",").filter { it.isNotBlank() }
            .map { it.trim() }

    private suspend fun getPlayers(
        username: String,
        namesToSearch: List<String>
    ): HttpResponse<List<PlayersWaiverResponse>> {
        val playersInWaiver = doGetPlayersInWaiver(username, namesToSearch)
        return if (playersInWaiver.count() > 0) {
            ok(playersInWaiver)
        } else {
            notFound()
        }
    }

    private suspend fun doGetPlayersInWaiver(
        username: String,
        namesToSearch: List<String>
    ) = getPlayersInWaiver.get(username, namesToSearch)
        .map { (player, leagues) ->
            PlayersWaiverResponse(player, leagues)
        }
}
