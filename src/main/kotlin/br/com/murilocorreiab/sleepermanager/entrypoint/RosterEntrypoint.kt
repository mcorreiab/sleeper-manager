package br.com.murilocorreiab.sleepermanager.entrypoint

import br.com.murilocorreiab.sleepermanager.adapters.NotFoundException
import br.com.murilocorreiab.sleepermanager.adapters.UnavailableStarterPlayersController
import br.com.murilocorreiab.sleepermanager.entrypoint.entity.RosterResponse
import br.com.murilocorreiab.sleepermanager.entrypoint.entity.RosterResponseMapperNew
import br.com.murilocorreiab.sleepermanager.usecase.GetUnavailableStarterPlayers
import br.com.murilocorreiab.sleepermanager.usecase.LeagueGateway
import br.com.murilocorreiab.sleepermanager.usecase.PlayerGateway
import br.com.murilocorreiab.sleepermanager.usecase.RosterGateway2
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
import org.mapstruct.factory.Mappers

@Controller("/rosters")
class RosterEntrypoint(
    leagueGateway: LeagueGateway,
    rosterGateway: RosterGateway2,
    playerGateway: PlayerGateway,
) {
    private val rosterResponseMapper = Mappers.getMapper(RosterResponseMapperNew::class.java)

    private val unavailableStarterPlayersController: UnavailableStarterPlayersController

    init {
        val getUnavailableStarterPlayers = GetUnavailableStarterPlayers(leagueGateway, rosterGateway, playerGateway)
        unavailableStarterPlayersController = UnavailableStarterPlayersController(getUnavailableStarterPlayers)
    }

    @Operation(
        summary = "Get unavailable players",
        description = "Get all unavailable players in starter position in each of user's rosters by userId",
    )
    @ApiResponses(
        ApiResponse(
            description = "At least one of the rosters have one unavailable player",
            content = [Content(array = ArraySchema(schema = Schema(implementation = RosterResponse::class)))],
        ),
        ApiResponse(responseCode = "404", description = "No unavailable player was found"),
        ApiResponse(responseCode = "500", description = "An unexpected error occurred"),
    )
    @Get("/user/userId/{userId}/unavailable")
    fun recoverRostersWithUnavailablePlayersByUserId(@PathVariable userId: String): HttpResponse<List<RosterResponse>> =
        try {
            ok(unavailableStarterPlayersController.getByUserId(userId).let { rosterResponseMapper.fromDomain(it) })
        } catch (e: NotFoundException) {
            notFound()
        }
}
