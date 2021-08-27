package br.com.murilocorreiab.sleepermanager.dataprovider.roster

import br.com.murilocorreiab.sleepermanager.dataprovider.league.entity.LeagueResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.league.entity.UserResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.LeagueClient
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.UserClient
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueResponse
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.UserResponse
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.entity.RosterResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.RosterClient
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.RosterResponse
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class GetRostersInUserLeaguesHttpTest {

    @InjectMockKs
    private lateinit var target: GetRostersInUserLeaguesHttp

    @MockK
    private lateinit var userClient: UserClient

    @MockK
    private lateinit var leagueClient: LeagueClient

    @MockK
    private lateinit var rosterClient: RosterClient

    @Test
    fun `should get all rosters with success`() {
        // Given
        val username = "username"
        val userResponse = UserResponseProducer().build()
        val leagueResponse = LeagueResponseProducer().build()
        val rosterResponse = RosterResponseProducer.build()
        val rostersByLeague = listOf(rosterResponse)

        // When
        every { userClient.getByUsername(username) } returns userResponse
        whenGetRostersAndLeague(userResponse, leagueResponse, rostersByLeague)
        val actual = target.getAllRosters(username).toList()

        // Then
        assertThatGetRosters(actual, leagueResponse, rosterResponse)
    }

    @Test
    fun `should get user rosters by username with success`() {
        // Given
        val username = "username"
        val userId = "userId"
        val userResponse = UserResponseProducer(userId = userId).build()
        val leagueResponse = LeagueResponseProducer().build()
        val userRoster = RosterResponseProducer.build(ownerId = userId, rosterId = "roster1")
        val otherUserRoster = RosterResponseProducer.build(ownerId = "otherUser", rosterId = "roster2")
        val rostersByLeague = listOf(userRoster, otherUserRoster)

        // When
        every { userClient.getByUsername(username) } returns userResponse
        whenGetRostersAndLeague(userResponse, leagueResponse, rostersByLeague)
        val actual = target.getUserRosters(username).toList()

        // Then
        assertThatGetRosters(actual, leagueResponse, userRoster)
    }

    @Test
    fun `should get user rosters by userId with success`() {
        // Given
        val userId = "userId"
        val userResponse = UserResponseProducer(userId = userId).build()
        val leagueResponse = LeagueResponseProducer().build()
        val userRoster = RosterResponseProducer.build(ownerId = userId, rosterId = "roster1")
        val otherUserRoster = RosterResponseProducer.build(ownerId = "otherUser", rosterId = "roster2")
        val rostersByLeague = listOf(userRoster, otherUserRoster)

        // When
        whenGetRostersAndLeague(userResponse, leagueResponse, rostersByLeague)
        val actual = target.getUserRostersById(userId)

        // Then
        assertThatGetRosters(actual, leagueResponse, userRoster)
    }

    private fun whenGetRostersAndLeague(
        userResponse: UserResponse,
        leagueResponse: LeagueResponse,
        rostersByLeague: List<RosterResponse>
    ) {
        every { leagueClient.getByUserId(userResponse.userId) } returns listOf(leagueResponse)
        every { rosterClient.getRostersOfALeague(leagueResponse.leagueId) } returns
            rostersByLeague
    }

    private fun assertThatGetRosters(
        rostersByLeague: List<Pair<LeagueResponse, List<RosterResponse>>>,
        leagueResponse: LeagueResponse,
        userRoster: RosterResponse
    ) {
        assertEquals(1, rostersByLeague.size)
        assertEquals(leagueResponse.leagueId, rostersByLeague[0].first.leagueId)
        assertEquals(1, rostersByLeague[0].second.toList().size)
        assertEquals(userRoster.rosterId, rostersByLeague[0].second.toList()[0].rosterId)
    }
}
