package br.com.murilocorreiab.sleepermanager.entrypoint

import br.com.murilocorreiab.sleepermanager.config.WireMockTest
import br.com.murilocorreiab.sleepermanager.entrypoint.client.RosterClient
import br.com.murilocorreiab.sleepermanager.entrypoint.entity.RosterResponse
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.HttpStatus.NOT_FOUND
import io.micronaut.http.MediaType
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@MicronautTest
@WireMockTest
class RosterEntrypointTest {

    @Inject
    private lateinit var rosterClient: RosterClient

    @Test
    fun `should recover unavailable players for a user using username`() {
        // Given
        val username = "murilocorreia"
        val userId = "303333123121229824"

        // When
        arrangeToRecoverRosterOfAUserByUsername(username, userId)
        val actual = rosterClient.recoverRosterOfAUserByUsername(username)

        // Then
        assertThatFoundUnavailablePlayer(actual)
    }

    @Test
    fun `if user don't have unavailable player when searching for username return 404`() {
        // Given
        val username = "fernandocorreia"
        val userId = "336162535059259392"

        arrangeToRecoverRosterOfAUserByUsername(username, userId)
        val response = rosterClient.recoverRosterOfAUserByUsername(username)
        assertEquals(NOT_FOUND, response.status)
    }

    private fun arrangeToRecoverRosterOfAUserByUsername(username: String, userId: String) {
        // When
        stubFor(
            get(urlEqualTo("/user/$username"))
                .willReturn(
                    aResponse().withHeader("content-type", MediaType.APPLICATION_JSON)
                        .withBodyFile("user_response_$username.json")
                )
        )

        arrangeToRecoverRosterOfAUser(userId)
    }

    @Test
    fun `should recover unavailable players for a user using id`() {
        // Given
        val userId = "303333123121229824"

        // When
        arrangeToRecoverRosterOfAUser(userId)
        val actual = rosterClient.recoverRosterOfAUserById(userId)

        // Then
        assertThatFoundUnavailablePlayer(actual)
    }

    @Test
    fun `if user don't have unavailable player when searching for userId return 404`() {
        // Given
        val userId = "336162535059259392"

        arrangeToRecoverRosterOfAUser(userId)
        val response = rosterClient.recoverRosterOfAUserById(userId)
        assertEquals(NOT_FOUND, response.status)
    }

    private fun arrangeToRecoverRosterOfAUser(userId: String) {
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
    }

    private fun assertThatFoundUnavailablePlayer(actual: HttpResponse<List<RosterResponse>>) {
        assertEquals(HttpStatus.OK.code, actual.status.code)
        val body = actual.body()
        assertTrue(body?.size == 1)
        assertTrue(body?.get(0)?.players?.size == 1)
        assertTrue(body?.any { it.players.any { player -> player.name == "Saquon Barkley" } } ?: false)
    }
}
