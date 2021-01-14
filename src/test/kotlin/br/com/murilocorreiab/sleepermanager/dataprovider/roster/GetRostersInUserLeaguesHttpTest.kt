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
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.inject.Inject

@MicronautTest
class GetRostersInUserLeaguesHttpTest {

    @Inject
    lateinit var target: GetRostersInUserLeaguesHttp

    @get:MockBean(UserClient::class)
    val userClient = mockk<UserClient>()

    @get:MockBean(LeagueClient::class)
    val leagueClient = mockk<LeagueClient>()

    @get:MockBean(RosterClient::class)
    val rosterClient = mockk<RosterClient>()

    @ExperimentalCoroutinesApi
    @Test
    fun `should get all rosters with success`() = runBlockingTest {
        // Given
        val username = "username"
        val userResponse = UserResponseProducer().build()
        val leagueResponse = LeagueResponseProducer().build()
        val rosterResponse = RosterResponseProducer().build()
        val rostersByLeague = Flux.just(rosterResponse)

        // When
        every { userClient.getByUsername(username) } returns Mono.just(userResponse)
        whenGetRostersAndLeague(userResponse, leagueResponse, rostersByLeague)
        val actual = target.getAllRosters(username).toList()

        // Then
        assertThatGetRosters(actual, leagueResponse, rosterResponse)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should get user rosters by username with success`() = runBlockingTest {
        // Given
        val username = "username"
        val userId = "userId"
        val userResponse = UserResponseProducer(userId = userId).build()
        val leagueResponse = LeagueResponseProducer().build()
        val userRoster = RosterResponseProducer(ownerId = userId, rosterId = "roster1").build()
        val otherUserRoster = RosterResponseProducer(ownerId = "otherUser", rosterId = "roster2").build()
        val rostersByLeague = Flux.just(userRoster, otherUserRoster)

        // When
        every { userClient.getByUsername(username) } returns Mono.just(userResponse)
        whenGetRostersAndLeague(userResponse, leagueResponse, rostersByLeague)
        val actual = target.getUserRosters(username).toList()

        // Then
        assertThatGetRosters(actual, leagueResponse, userRoster)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should get user rosters by userId with success`() = runBlockingTest {
        // Given
        val userId = "userId"
        val userResponse = UserResponseProducer(userId = userId).build()
        val leagueResponse = LeagueResponseProducer().build()
        val userRoster = RosterResponseProducer(ownerId = userId, rosterId = "roster1").build()
        val otherUserRoster = RosterResponseProducer(ownerId = "otherUser", rosterId = "roster2").build()
        val rostersByLeague = Flux.just(userRoster, otherUserRoster)

        // When
        whenGetRostersAndLeague(userResponse, leagueResponse, rostersByLeague)
        val actual = target.getUserRostersById(userId)

        // Then
        assertThatGetRosters(actual, leagueResponse, userRoster)
    }

    private fun whenGetRostersAndLeague(
        userResponse: UserResponse,
        leagueResponse: LeagueResponse,
        rostersByLeague: Flux<RosterResponse>
    ) {
        every { leagueClient.getByUserId(userResponse.userId) } returns Flux.just(leagueResponse)
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
