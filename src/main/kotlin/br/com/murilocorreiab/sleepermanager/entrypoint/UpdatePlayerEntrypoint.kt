package br.com.murilocorreiab.sleepermanager.entrypoint

import br.com.murilocorreiab.sleepermanager.domain.player.usecase.UpdatePlayer
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpResponse.noContent
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Status
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import kotlinx.coroutines.runBlocking

@Controller("/players")
class UpdatePlayerEntrypoint(private val updatePlayer: UpdatePlayer) {

    @Operation(
        summary = "Update players in internal database",
        description = "Update all players in database"
    )
    @ApiResponse(description = "Players updated with success")
    @Post("/update")
    @Status(HttpStatus.NO_CONTENT)
    fun update(): HttpResponse<Unit> = runBlocking {
        updatePlayer.updatePlayers()
        noContent()
    }
}
