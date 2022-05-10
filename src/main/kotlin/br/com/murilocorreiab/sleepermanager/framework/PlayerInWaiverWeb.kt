package br.com.murilocorreiab.sleepermanager.framework

import br.com.murilocorreiab.sleepermanager.adapters.BadRequestException
import br.com.murilocorreiab.sleepermanager.adapters.NotFoundException
import br.com.murilocorreiab.sleepermanager.adapters.league.LeagueClient
import br.com.murilocorreiab.sleepermanager.adapters.league.LeagueGatewayImpl
import br.com.murilocorreiab.sleepermanager.adapters.player.PlayerClient
import br.com.murilocorreiab.sleepermanager.adapters.player.PlayerGatewayImpl
import br.com.murilocorreiab.sleepermanager.adapters.player.PlayerInWaiverController
import br.com.murilocorreiab.sleepermanager.adapters.player.PlayerRepository
import br.com.murilocorreiab.sleepermanager.adapters.player.PlayersWaiverResponse
import br.com.murilocorreiab.sleepermanager.adapters.roster.RosterClient
import br.com.murilocorreiab.sleepermanager.adapters.roster.RosterGatewayImpl
import br.com.murilocorreiab.sleepermanager.usecase.GetPlayersOutOfRoster
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

@Controller("/players")
class PlayerInWaiverWeb(
    leagueClient: LeagueClient,
    playerClient: PlayerClient,
    playerRepository: PlayerRepository,
    rosterClient: RosterClient,
) {

    private val playerInWaiverController: PlayerInWaiverController

    init {
        val leagueGateway = LeagueGatewayImpl(leagueClient)
        val playerGateway = PlayerGatewayImpl(playerClient, playerRepository)
        val rosterGateway = RosterGatewayImpl(rosterClient)
        val getPlayersOutOfRoster = GetPlayersOutOfRoster(leagueGateway, playerGateway, rosterGateway)
        playerInWaiverController = PlayerInWaiverController(getPlayersOutOfRoster)
    }

    @Operation(
        summary = "Get Players in waiver",
        description = "Get all leagues in which at least one of a list of players is available",
    )
    @ApiResponses(
        ApiResponse(
            description = "One or more players are available in one or more league's waivers",
            content = [Content(array = ArraySchema(schema = Schema(implementation = PlayersWaiverResponse::class)))],
        ),
        ApiResponse(responseCode = "400", description = "Should inform one or more players"),
        ApiResponse(responseCode = "404", description = "Couldn't find any of the informed players in waivers"),
    )
    @Get("/user/{userId}/waiver{?players}")
    fun getPlayersInWaiverByLeague(userId: String, players: String?) = try {
        HttpResponse.ok(playerInWaiverController.getPlayersInWaiverByLeague(userId, players))
    } catch (e: NotFoundException) {
        HttpResponse.notFound()
    } catch (e: BadRequestException) {
        HttpResponse.badRequest()
    }
}
