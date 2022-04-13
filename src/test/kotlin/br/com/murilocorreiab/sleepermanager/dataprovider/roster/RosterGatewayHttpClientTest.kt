package br.com.murilocorreiab.sleepermanager.dataprovider.roster

import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueResponse
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.UserResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.GetPlayers
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.entity.RosterResponseFactory
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.RosterResponse
import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerProducer
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

@ExtendWith(MockKExtension::class)
class RosterGatewayHttpClientTest {

    @InjectMockKs
    private lateinit var target: RosterGatewayHttpClient

    @MockK
    private lateinit var getRostersInUserLeagues: GetRostersInUserLeagues

    @MockK
    private lateinit var getPlayers: GetPlayers

    private val starterPlayerId = "starterPlayerId"
    private val benchPlayerId = "benchPlayerId"
    private val playerNonexistentId = "playerNonexistentId"
    private val username = "username"

    @Test
    fun `should get rosters for user with success`() {
        // Given
        val username = "username"
        val userResponse = UserResponseProducer.build()
        val leagueResponse = LeagueResponseProducer.build()
        val rosterResponse = RosterResponseFactory.build(
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
        val leagueResponse = LeagueResponseProducer.build()
        val rosterResponse = RosterResponseFactory.build(
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
        every { getPlayers.getPlayerById(playerNonexistentId) } returns null
    }

    private fun createPlayerRepositoryMock(playerId: String) {
        every { getPlayers.getPlayerById(playerId) } returns PlayerProducer.build(id = playerId, name = playerId)
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
        val leagueResponse = LeagueResponseProducer.build()
        val rosterResponse = RosterResponseFactory.build(players = listOf(playerNonexistentId))

        // When
        every { getRostersInUserLeagues.getUserRosters(username) } returns listOf(
            Pair(
                leagueResponse,
                listOf(rosterResponse)
            )
        )
        every { getPlayers.getPlayerById(playerNonexistentId) } returns null

        val actual = target.findUserRostersByUsernameInLeagues(username)

        // Then
        assertTrue(actual.isEmpty())
    }

    @Test
    fun `given that the roster has no players should return an empty list`() {
        // Given
        val leagueResponse = LeagueResponseProducer.build()
        val rosterResponse = RosterResponseFactory.build(players = null)

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
}
