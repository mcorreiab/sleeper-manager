package br.com.murilocorreiab.sleepermanager.entrypoint

import br.com.murilocorreiab.sleepermanager.entrypoint.client.PlayerInWaiverClient
import br.com.murilocorreiab.sleepermanager.util.Wiremock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class PlayerInWaiverEntrypointTest {

    @Inject
    private lateinit var playerInWaiverClient: PlayerInWaiverClient

    @Inject
    private lateinit var wireMock: Wiremock

    @Test
    fun `should get players in waiver with success`() {
        // Given
        val userName = "username"
        val player1ToSearch = "Aaron"
        val player2ToSearch = "Davante"
        val playersToSearch = "$player1ToSearch, $player2ToSearch"

        // When
        stubFor(
            get(urlEqualTo("/user/$userName")).willReturn(
                aResponse().withHeader("content-type", MediaType.APPLICATION_JSON)
                    .withBodyFile("user_response.json")
            )
        )

        stubFor(
            get(urlEqualTo("/user/303333123121229824/leagues/nfl/2020"))
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

        stubFor(
            get(urlEqualTo("/players/nfl"))
                .willReturn(
                    aResponse().withHeader(
                        "content-type",
                        MediaType.APPLICATION_JSON
                    ).withBodyFile("players_response.json")
                )
        )

        val response = playerInWaiverClient.getPlayersInWaiverByLeague(userName, playersToSearch)

        // Then
        assertEquals(HttpStatus.OK, response.status)

        val body = response.body() ?: throw Exception("Should had returned a player")
        val playerFoundWaiver = body[0]
        val leagues = playerFoundWaiver.leagues
        assertEquals(1, body.size)
        assertEquals("4199", playerFoundWaiver.player.id)
        assertEquals(1, leagues.size)
        assertEquals("602534189945909248", leagues[0].id)
    }

    @Test
    fun `if no player is passed as param return 404`() {
        // Given
        val userName = "username"

        // When
        val response = playerInWaiverClient.getPlayersInWaiverByLeague(userName, null)

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.status)
    }

    @Test
    fun `if player list is empty return 404`() {
        // Given
        val userName = "username"

        // When
        val response = playerInWaiverClient.getPlayersInWaiverByLeague(userName, "")

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.status)
    }
}
