package br.com.murilocorreiab.sleepermanager.framework

import br.com.murilocorreiab.sleepermanager.adapters.NotFoundException
import br.com.murilocorreiab.sleepermanager.adapters.UnavailableStarterPlayersController
import br.com.murilocorreiab.sleepermanager.adapters.league.LeagueClient
import br.com.murilocorreiab.sleepermanager.adapters.league.LeagueExternalResponseMapper
import br.com.murilocorreiab.sleepermanager.adapters.league.LeagueGatewayImpl
import br.com.murilocorreiab.sleepermanager.adapters.player.PlayerClient
import br.com.murilocorreiab.sleepermanager.adapters.player.PlayerExternalResponseMapper
import br.com.murilocorreiab.sleepermanager.adapters.player.PlayerGatewayImpl
import br.com.murilocorreiab.sleepermanager.adapters.player.PlayerRepository
import br.com.murilocorreiab.sleepermanager.adapters.roster.RosterClient
import br.com.murilocorreiab.sleepermanager.adapters.roster.RosterExternalResponseMapper
import br.com.murilocorreiab.sleepermanager.adapters.roster.RosterGatewayImpl
import br.com.murilocorreiab.sleepermanager.adapters.roster.RosterResponse
import br.com.murilocorreiab.sleepermanager.adapters.roster.RosterResponseMapper
import br.com.murilocorreiab.sleepermanager.usecase.GetUnavailableStarterPlayers
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

@Controller("/rosters")
class RostersWeb(
    leagueClient: LeagueClient,
    playerClient: PlayerClient,
    playerRepository: PlayerRepository,
    rosterClient: RosterClient,
    private val rosterResponseMapper: RosterResponseMapper,
    leagueMapper: LeagueExternalResponseMapper,
    playerResponseMapper: PlayerExternalResponseMapper,
    rosterExternalResponseMapper: RosterExternalResponseMapper,
) {

    private val unavailableStarterPlayersController: UnavailableStarterPlayersController

    init {
        val leagueGateway = LeagueGatewayImpl(leagueClient, leagueMapper)
        val playerGateway = PlayerGatewayImpl(playerClient, playerRepository, playerResponseMapper)
        val rosterGateway = RosterGatewayImpl(rosterClient, rosterExternalResponseMapper)
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
