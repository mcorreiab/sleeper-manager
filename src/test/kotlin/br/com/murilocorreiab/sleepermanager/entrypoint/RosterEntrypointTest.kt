package br.com.murilocorreiab.sleepermanager.entrypoint

import br.com.murilocorreiab.sleepermanager.config.WireMockTest
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster
import br.com.murilocorreiab.sleepermanager.entrypoint.client.RosterClient
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.HttpStatus.NOT_FOUND
import io.micronaut.http.MediaType
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
@WireMockTest
class RosterEntrypointTest {

    @Inject
    private lateinit var rosterClient: RosterClient

    @Test
    fun `should recover unavailable players for a user`() {
        // Given
        val username = "murilocorreia"
        val userId = "303333123121229824"

        // When
        val actual = recoverRosterOfAUser(username, userId)

        // Then
        assertEquals(HttpStatus.OK.code, actual.status.code)
        val body = actual.body()
        assertTrue(body?.size == 1)
        assertTrue(body?.get(0)?.players?.size == 1)
        assertTrue(body?.any { it.players.any { player -> player.name == "Saquon Barkley" } } ?: false)
    }

    @Test
    fun `if user don't have unavailable player return 404`() {
        // Given
        val username = "fernandocorreia"
        val userId = "336162535059259392"

        val response = recoverRosterOfAUser(username, userId)
        assertEquals(NOT_FOUND, response.status)
    }

    private fun recoverRosterOfAUser(username: String, userId: String): HttpResponse<List<Roster>> {
        // When
        stubFor(
            get(urlEqualTo("/user/$username"))
                .willReturn(
                    aResponse().withHeader("content-type", MediaType.APPLICATION_JSON)
                        .withBodyFile("user_response_$username.json")
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

        stubFor(
            get(urlEqualTo("/user/$userId/leagues/nfl/2020"))
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

        return rosterClient.recoverRosterOfAUser(username)
    }
}
