package br.com.murilocorreiab.sleepermanager.entrypoint

import br.com.murilocorreiab.sleepermanager.config.WireMockTest
import br.com.murilocorreiab.sleepermanager.entrypoint.client.PlayerInWaiverClient
import br.com.murilocorreiab.sleepermanager.entrypoint.entity.PlayersWaiverResponse
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus.BAD_REQUEST
import io.micronaut.http.HttpStatus.NOT_FOUND
import io.micronaut.http.HttpStatus.OK
import io.micronaut.http.MediaType
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@MicronautTest
@WireMockTest
class PlayerInWaiverEntrypointTest {

    @Inject
    private lateinit var playerInWaiverClient: PlayerInWaiverClient

    private val userId = "303333123121229824"

    @Test
    fun `should get players in waiver with success`() {
        // Given
        val player1ToSearch = "Aaron"
        val player2ToSearch = "Davante"
        val playersToSearch = "$player1ToSearch, $player2ToSearch,  "

        // When
        val response = searchForPlayers(playersToSearch)

        // Then
        assertEquals(OK, response.status)

        val body = response.body() ?: throw Exception("Should had returned a player")
        val playerFoundWaiver = body[0]
        val leagues = playerFoundWaiver.leagues
        assertEquals(1, body.size)
        assertEquals("4199", playerFoundWaiver.player.id)
        assertEquals(1, leagues.size)
        assertEquals("602534189945909248", leagues[0].id)
    }

    @Test
    fun `if cant find any player being searched return 404`() {
        // Given
        val player1ToSearch = "Barkley"
        val player2ToSearch = "Watt"
        val playersToSearch = "$player1ToSearch, $player2ToSearch"

        // When
        val response = searchForPlayers(playersToSearch)

        // Then
        assertEquals(NOT_FOUND, response.status)
    }

    private fun searchForPlayers(playersToSearch: String): HttpResponse<List<PlayersWaiverResponse>> {
        // When
        stubFor(
            get(urlEqualTo("/players/nfl"))
                .willReturn(
                    aResponse().withHeader(
                        "content-type",
                        MediaType.APPLICATION_JSON
                    ).withBodyFile("players_response.json")
                )
        )

        stubFor(
            get(urlEqualTo("/user/$userId/leagues/nfl/2021"))
                .willReturn(
                    aResponse().withHeader("content-type", MediaType.APPLICATION_JSON)
                        .withBodyFile("league_response.json")
                )
        )

        stubFor(
            get(urlEqualTo("/league/602534189945909248/rosters"))
                .willReturn(
                    aResponse().withHeader("content-type", MediaType.APPLICATION_JSON)
                        .withBodyFile("roster_response.json")
                )
        )

        return playerInWaiverClient.getPlayersInWaiverByLeague(userId, playersToSearch)
    }

    @Test
    fun `if no player is passed as param return 400`() {
        // When
        try {
            playerInWaiverClient.getPlayersInWaiverByLeague(userId, null)
            throw Exception("Should had throw an error")
        } catch (ex: HttpClientResponseException) {
            assertEquals(BAD_REQUEST, ex.status)
        }
    }

    @Test
    fun `if player list is empty return 400`() {
        // When
        try {
            playerInWaiverClient.getPlayersInWaiverByLeague(userId, "")
            throw Exception("Should had throw an error")
        } catch (ex: HttpClientResponseException) {
            assertEquals(BAD_REQUEST, ex.status)
        }
    }
}
