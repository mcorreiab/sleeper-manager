package br.com.murilocorreiab.sleepermanager.dataprovider.roster

import br.com.murilocorreiab.sleepermanager.dataprovider.league.entity.LeagueResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.league.entity.UserResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.LeagueClient
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.UserClient
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.entity.PlayerResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.entity.RosterResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.PlayerClient
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.RosterClient
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockkClass
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class RosterGatewayHttpClientTest {

    @Inject
    private lateinit var rosterGatewayHttpClient: RosterGatewayHttpClient

    @Inject
    private lateinit var userClient: UserClient

    @Inject
    private lateinit var leagueClient: LeagueClient

    @Inject
    private lateinit var rosterClient: RosterClient

    @Inject
    private lateinit var playerClient: PlayerClient

    @FlowPreview
    @Test
    fun `should get rosters for user with success`() = runBlocking {
        // Given
        val username = "username"
        val starterPlayerId = "starterPlayerId"
        val benchPlayerId = "benchPlayerId"
        val userResponse = UserResponseProducer().build()
        val leagueResponse = LeagueResponseProducer().build()
        val rosterResponse = RosterResponseProducer(
            starters = listOf(starterPlayerId),
            players = listOf(starterPlayerId, benchPlayerId),
            ownerId = userResponse.userId
        ).build()
        val rosterOfAnotherPlayer = RosterResponseProducer(ownerId = "otherPlayer").build()
        val starterPlayerResponse =
            PlayerResponseProducer(playerId = starterPlayerId, firstName = starterPlayerId).build()
        val benchPlayerResponse = PlayerResponseProducer(playerId = benchPlayerId, firstName = benchPlayerId).build()

        // When
        every { userClient.getByUsername(username) }.returns(userResponse)
        every { leagueClient.getByUserId(userResponse.userId) }.returns(flowOf(leagueResponse))
        every { rosterClient.getRostersOfALeague(leagueResponse.leagueId) }.returns(
            flowOf(
                rosterResponse,
                rosterOfAnotherPlayer
            )
        )
        every { playerClient.getAllPlayers() }.returns(flowOf(starterPlayerResponse, benchPlayerResponse))

        val actual = rosterGatewayHttpClient.findUserRostersInLeagues(username)

        // Then
        assertTrue(actual.toList().isNotEmpty())
        actual.collect {
            assertEquals(rosterResponse.rosterId, it.id)
            assertEquals(leagueResponse.leagueId, it.league.id)
            assertEquals(userResponse.userId, it.ownerId)
            assertTrue(it.players.first { player -> player.id == starterPlayerId }.isStarter)
            assertFalse(it.players.first { player -> player.id == benchPlayerId }.isStarter)
        }
    }

    @MockBean(UserClient::class)
    fun userClient() = mockkClass(UserClient::class)

    @MockBean(LeagueClient::class)
    fun leagueClient() = mockkClass(LeagueClient::class)

    @MockBean(RosterClient::class)
    fun rosterClient() = mockkClass(RosterClient::class)

    @MockBean(PlayerClient::class)
    fun playerClient() = mockkClass(PlayerClient::class)
}
