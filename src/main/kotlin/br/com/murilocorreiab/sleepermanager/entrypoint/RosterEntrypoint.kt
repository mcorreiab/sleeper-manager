package br.com.murilocorreiab.sleepermanager.entrypoint

import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster
import br.com.murilocorreiab.sleepermanager.domain.roster.usecase.GetRostersWithUnavailablePlayers
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpResponse.notFound
import io.micronaut.http.HttpResponse.ok
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.runBlocking

@Controller("/rosters")
class RosterEntrypoint(private val getRostersWithUnavailablePlayers: GetRostersWithUnavailablePlayers) {

    @Operation(
        summary = "Get unavailable players",
        description = "Get all unavailable players in starter position in each of user's rosters"
    )
    @ApiResponses(
        ApiResponse(
            description = "At least one of the rosters have one unavailable player",
            content = [Content(array = ArraySchema(schema = Schema(implementation = Roster::class)))]
        ),
        ApiResponse(responseCode = "404", description = "No unavailable player was found"),
        ApiResponse(responseCode = "500", description = "An unexpected error occurred"),
    )
    @Get("/user/{username}/unavailable")
    fun recoverRostersWithUnavailablePlayers(@PathVariable username: String): HttpResponse<Flow<Roster>> =
        runBlocking {
            val rosters = getRostersWithUnavailablePlayers.get(username)
            if (rosters.count() > 0) {
                ok(rosters)
            } else {
                notFound()
            }
        }
}
