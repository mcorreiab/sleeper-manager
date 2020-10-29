package br.com.murilocorreiab.sleepermanager.dataprovider.roster

import br.com.murilocorreiab.sleepermanager.dataprovider.league.entity.LeagueResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.league.entity.UserResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.LeagueClient
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.UserClient
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.entity.PlayerResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.entity.RosterResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.PlayerClient
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.RosterClient
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.RosterResponse
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockkClass
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
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
    private lateinit var target: RosterGatewayHttpClient

    @Inject
    private lateinit var userClient: UserClient

    @Inject
    private lateinit var leagueClient: LeagueClient

    @Inject
    private lateinit var rosterClient: RosterClient

    @Inject
    private lateinit var playerClient: PlayerClient

    private val username = "username"
    private val starterPlayerId = "starterPlayerId"
    private val benchPlayerId = "benchPlayerId"
    private val userResponse = UserResponseProducer().build()
    private val leagueResponse = LeagueResponseProducer().build()
    private val rosterResponse = RosterResponseProducer(
        starters = listOf(starterPlayerId),
        players = listOf(starterPlayerId, benchPlayerId),
        ownerId = userResponse.userId
    ).build()
    private val rosterOfAnotherPlayer = RosterResponseProducer(ownerId = "otherPlayer").build()
    private val starterPlayerResponse =
        PlayerResponseProducer(playerId = starterPlayerId, firstName = starterPlayerId).build()
    private val benchPlayerResponse =
        PlayerResponseProducer(playerId = benchPlayerId, firstName = benchPlayerId).build()

    @FlowPreview
    @Test
    fun `should get rosters for user with success`() = runBlocking {
        // When
        val players = flowOf(rosterResponse, rosterOfAnotherPlayer)
        arrangeToDoFullFlow(players)

        val actual = target.findUserRostersInLeagues(username)

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

    @FlowPreview
    @Test
    fun `if roster has no official players return empty flow`() = runBlocking {
        // When
        arrangeToDoFullFlow(emptyFlow())

        val actual = target.findUserRostersInLeagues(username)

        // Then
        assertTrue(actual.toList().isEmpty())
    }

    private fun arrangeToDoFullFlow(players: Flow<RosterResponse>) {
        every { userClient.getByUsername(username) }.returns(userResponse)
        every { leagueClient.getByUserId(userResponse.userId) }.returns(flowOf(leagueResponse))
        every { rosterClient.getRostersOfALeague(leagueResponse.leagueId) }.returns(players)
        every { playerClient.getAllPlayers() }.returns(flowOf(starterPlayerResponse, benchPlayerResponse))
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
