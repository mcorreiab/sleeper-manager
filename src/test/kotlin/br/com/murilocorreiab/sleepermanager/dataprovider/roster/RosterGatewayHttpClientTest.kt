package br.com.murilocorreiab.sleepermanager.dataprovider.roster

import br.com.murilocorreiab.sleepermanager.dataprovider.league.entity.LeagueResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.league.entity.UserResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueResponse
import br.com.murilocorreiab.sleepermanager.dataprovider.player.db.PlayerRepository
import br.com.murilocorreiab.sleepermanager.dataprovider.player.db.entity.PlayerDbProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.entity.RosterResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.RosterResponse
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.Optional

@ExtendWith(MockKExtension::class)
class RosterGatewayHttpClientTest {

    @InjectMockKs
    private lateinit var target: RosterGatewayHttpClient

    @MockK
    private lateinit var getRostersInUserLeagues: GetRostersInUserLeagues

    @MockK
    private lateinit var playerRepository: PlayerRepository

    private val starterPlayerId = "starterPlayerId"
    private val benchPlayerId = "benchPlayerId"
    private val playerNonexistentId = "playerNonexistentId"
    private val username = "username"

    @Test
    fun `should get rosters for user with success`() {
        // Given
        val username = "username"
        val userResponse = UserResponseProducer().build()
        val leagueResponse = LeagueResponseProducer().build()
        val rosterResponse = RosterResponseProducer.build(
            starters = listOf(starterPlayerId),
            players = listOf(starterPlayerId, benchPlayerId, playerNonexistentId),
            ownerId = userResponse.userId
        )

        // When
        every { getRostersInUserLeagues.getUserRosters(username) } returns listOf(
            Pair(
                leagueResponse,
                listOf(rosterResponse)
            )
        )
        createAllTypesPlayerRepositoryMock()

        val actual = target.findUserRostersByUsernameInLeagues(username)

        // Then
        assertThatFoundPlayersForUserRoster(actual, rosterResponse, leagueResponse, userResponse.userId)
    }

    @Test
    fun `should get players by user id with success`() {
        // Given
        val userId = "userId"
        val leagueResponse = LeagueResponseProducer().build()
        val rosterResponse = RosterResponseProducer.build(
            starters = listOf(starterPlayerId),
            players = listOf(starterPlayerId, benchPlayerId, playerNonexistentId),
            ownerId = userId
        )

        // When
        every { getRostersInUserLeagues.getUserRostersById(userId) } returns listOf(
            Pair(
                leagueResponse,
                listOf(rosterResponse)
            )
        )
        createAllTypesPlayerRepositoryMock()

        val actual = target.findUserRostersByUserIdInLeagues(userId)

        assertThatFoundPlayersForUserRoster(actual, rosterResponse, leagueResponse, userId)
    }

    private fun createAllTypesPlayerRepositoryMock() {
        createPlayerRepositoryMock(starterPlayerId)
        createPlayerRepositoryMock(benchPlayerId)
        every { playerRepository.findById(playerNonexistentId) } returns Optional.empty()
    }

    private fun createPlayerRepositoryMock(playerId: String) {
        every { playerRepository.findById(playerId) } returns Optional.of(
            PlayerDbProducer(
                id = playerId,
                name = playerId
            ).build()
        )
    }

    private fun assertThatFoundPlayersForUserRoster(
        actual: List<Roster>,
        rosterResponse: RosterResponse,
        leagueResponse: LeagueResponse,
        userId: String
    ) {
        assertTrue(actual.isNotEmpty())
        actual.forEach {
            assertEquals(rosterResponse.rosterId, it.id)
            assertEquals(leagueResponse.leagueId, it.league.id)
            assertEquals(userId, it.ownerId)
            assertTrue(it.players.first { player -> player.id == starterPlayerId }.starter)
            assertFalse(it.players.first { player -> player.id == benchPlayerId }.starter)
        }
    }

    @Test
    fun `if roster has no official players return empty list`() {
        // Given
        val playerNonexistentId = "playerNonexistentId"
        val leagueResponse = LeagueResponseProducer().build()
        val rosterResponse = RosterResponseProducer.build(players = listOf(playerNonexistentId))

        // When
        every { getRostersInUserLeagues.getUserRosters(username) } returns listOf(
            Pair(
                leagueResponse,
                listOf(rosterResponse)
            )
        )
        every { playerRepository.findById(playerNonexistentId) } returns Optional.empty()

        val actual = target.findUserRostersByUsernameInLeagues(username)

        // Then
        assertTrue(actual.isEmpty())
    }

    @Test
    fun `given that the roster has no players should return an empty list`() {
        // Given
        val leagueResponse = LeagueResponseProducer().build()
        val rosterResponse = RosterResponseProducer.build(players = null)

        // When
        every { getRostersInUserLeagues.getUserRosters(username) } returns listOf(
            Pair(
                leagueResponse,
                listOf(rosterResponse)
            )
        )

        val actual = target.findUserRostersByUsernameInLeagues(username)

        // Then
        assertTrue(actual.isEmpty())
    }

    @Test
    fun `should get all rostered players with success`() {
        // Given
        val rosteredPlayerId = "rosteredPlayerId"
        val roster1 = RosterResponseProducer.build(rosterId = "roster1", players = listOf(rosteredPlayerId))
        val roster2 = roster1.copy(rosterId = "roster2")
        val roster3 = roster1.copy(rosterId = "roster3", players = null)
        val league = LeagueResponseProducer().build()

        // When
        every { getRostersInUserLeagues.getAllRosters(username) } returns listOf(
            league to listOf(
                roster1,
                roster2,
                roster3
            )
        )
        every { playerRepository.findById(rosteredPlayerId) } returns
            Optional.of(PlayerDbProducer(id = rosteredPlayerId).build())
        val actual = target.findAllRosteredPlayersInUserLeagues(username)

        // Then
        val rosteredPlayers = actual[0].second
        assertEquals(1, actual.size)
        assertEquals(1, rosteredPlayers.size)
        assertEquals(rosteredPlayerId, rosteredPlayers[0].id)
    }
}
