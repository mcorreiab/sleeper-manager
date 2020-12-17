package br.com.murilocorreiab.sleepermanager.dataprovider.roster

import br.com.murilocorreiab.sleepermanager.dataprovider.league.entity.LeagueResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.league.entity.UserResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.player.db.PlayerRepository
import br.com.murilocorreiab.sleepermanager.dataprovider.player.db.entity.PlayerDbProducer
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
        val playerNonexistentId = "playerNonexistentId"
        val userResponse = UserResponseProducer().build()
        val leagueResponse = LeagueResponseProducer().build()
        val rosterResponse = RosterResponseProducer(
            starters = listOf(starterPlayerId),
            players = listOf(starterPlayerId, benchPlayerId, playerNonexistentId),
            ownerId = userResponse.userId
        ).build()

        // When
        coEvery { getRostersInUserLeagues.getUserRosters(username) } returns listOf(
            Pair(
                leagueResponse,
                listOf(rosterResponse)
            )
        )
        createPlayerRepositoryMock(starterPlayerId)
        createPlayerRepositoryMock(benchPlayerId)
        every { playerRepository.findById(playerNonexistentId) } returns Optional.empty()

        val actual = target.findUserRostersInLeagues(username)

        // Then
        assertTrue(actual.isNotEmpty())
        actual.forEach {
            assertEquals(rosterResponse.rosterId, it.id)
            assertEquals(leagueResponse.leagueId, it.league.id)
            assertEquals(userResponse.userId, it.ownerId)
            assertTrue(it.players.first { player -> player.id == starterPlayerId }.starter)
            assertFalse(it.players.first { player -> player.id == benchPlayerId }.starter)
        }
    }

    private fun createPlayerRepositoryMock(playerId: String) {
        every { playerRepository.findById(playerId) } returns Optional.of(
            PlayerDbProducer(
                id = playerId,
                name = playerId
            ).build()
        )
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    @Test
    fun `if roster has no official players return empty flow`() = runBlockingTest {
        // Given
        val username = "username"
        val playerNonexistentId = "playerNonexistentId"
        val leagueResponse = LeagueResponseProducer().build()
        val rosterResponse = RosterResponseProducer(players = listOf(playerNonexistentId)).build()

        // When
        coEvery { getRostersInUserLeagues.getUserRosters(username) } returns listOf(
            Pair(
                leagueResponse,
                listOf(rosterResponse)
            )
        )
        every { playerRepository.findById(playerNonexistentId) } returns Optional.empty()

        val actual = target.findUserRostersInLeagues(username)

        // Then
        assertTrue(actual.isEmpty())
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
