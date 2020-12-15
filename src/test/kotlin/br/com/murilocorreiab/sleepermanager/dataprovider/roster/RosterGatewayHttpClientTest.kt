package br.com.murilocorreiab.sleepermanager.dataprovider.roster

import br.com.murilocorreiab.sleepermanager.dataprovider.league.entity.LeagueResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.league.entity.UserResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.player.db.PlayerRepository
import br.com.murilocorreiab.sleepermanager.dataprovider.player.db.entity.PlayerDbProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.PlayerClient
import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.entity.PlayerResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.entity.RosterResponseProducer
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.Optional
import javax.inject.Inject

@MicronautTest
class RosterGatewayHttpClientTest {

    @Inject
    private lateinit var target: RosterGatewayHttpClient

    @get:MockBean(PlayerClient::class)
    val playerClient = mockk<PlayerClient>()

    @get:MockBean(GetRostersInUserLeagues::class)
    val getRostersInUserLeagues = mockk<GetRostersInUserLeagues>()

    @get:MockBean(PlayerRepository::class)
    val playerRepository = mockk<PlayerRepository>()

    @FlowPreview
    @ExperimentalCoroutinesApi
    @Test
    fun `should get rosters for user with success`() = runBlockingTest {
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
        val playersById = listOf(
            PlayerResponseProducer(playerId = starterPlayerId, fullName = starterPlayerId).build(),
            PlayerResponseProducer(playerId = benchPlayerId, fullName = benchPlayerId).build(),
            PlayerResponseProducer(playerId = "outOfTheRoster").build()
        ).associateBy({ it.playerId }, { it })

        // When
        coEvery { getRostersInUserLeagues.getUserRosters(username) } returns listOf(
            Pair(
                leagueResponse,
                listOf(rosterResponse)
            )
        )
        every { playerClient.getAllPlayers() } returns playersById

        val actual = target.findUserRostersInLeagues(username)

        // Then
        assertTrue(actual.toList().isNotEmpty())
        actual.forEach {
            assertEquals(rosterResponse.rosterId, it.id)
            assertEquals(leagueResponse.leagueId, it.league.id)
            assertEquals(userResponse.userId, it.ownerId)
            assertTrue(it.players.first { player -> player.id == starterPlayerId }.starter)
            assertFalse(it.players.first { player -> player.id == benchPlayerId }.starter)
        }
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    @Test
    fun `if roster has no official players return empty flow`() = runBlockingTest {
        // Given
        val username = "username"
        val leagueResponse = LeagueResponseProducer().build()
        val rosterResponse = RosterResponseProducer().build()

        // When
        coEvery { getRostersInUserLeagues.getUserRosters(username) } returns listOf(
            Pair(
                leagueResponse,
                listOf(rosterResponse)
            )
        )
        every { playerClient.getAllPlayers() } returns emptyMap()

        val actual = target.findUserRostersInLeagues(username)

        // Then
        assertTrue(actual.toList().isEmpty())
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @Test
    fun `should get all rostered players with success`() = runBlockingTest {
        // Given
        val userName = "username"
        val rosteredPlayerId = "rosteredPlayerId"
        val roster1 = RosterResponseProducer(rosterId = "roster1", players = listOf(rosteredPlayerId)).build()
        val roster2 = roster1.copy(rosterId = "roster2")
        val league = LeagueResponseProducer().build()

        // When
        coEvery { getRostersInUserLeagues.getAllRosters(userName) } returns listOf(league to listOf(roster1, roster2))
        every { playerRepository.findById(rosteredPlayerId) } returns
            Optional.of(PlayerDbProducer(id = rosteredPlayerId).build())
        val actual = target.findAllRosteredPlayersInUserLeagues(userName)

        // Then
        val rosteredPlayers = actual[0].second
        assertEquals(1, actual.size)
        assertEquals(1, rosteredPlayers.size)
        assertEquals(rosteredPlayerId, rosteredPlayers[0].id)
    }
}
