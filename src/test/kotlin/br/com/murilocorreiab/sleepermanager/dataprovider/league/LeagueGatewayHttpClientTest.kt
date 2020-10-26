package br.com.murilocorreiab.sleepermanager.dataprovider.league

import br.com.murilocorreiab.sleepermanager.dataprovider.league.entity.LeagueResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.league.entity.UserResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.LeagueClient
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.UserClient
import br.com.murilocorreiab.sleepermanager.domain.league.entity.LeagueProducer
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verify
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class LeagueGatewayHttpClientTest {

    @Inject
    private lateinit var target: LeagueGatewayHttpClient

    @Inject
    private lateinit var userClient: UserClient

    @Inject
    private lateinit var leagueClient: LeagueClient

    @Test
    fun `should retrieve data with success`() = runBlocking {
        // Given
        val username = "username"
        val userResponse = UserResponseProducer().build()
        val leagueResponse = LeagueResponseProducer().build()
        val league = LeagueProducer(
            name = leagueResponse.name,
            size = leagueResponse.totalRosters,
            id = leagueResponse.leagueId
        ).build()

        // When
        every { userClient.getByUsername(username) }.returns(userResponse)
        every { leagueClient.getByUserId(userResponse.userId) }.returns(flowOf(leagueResponse))
        val actual = target.findUserLeagues(username)

        // Then
        actual.collect {
            assertEquals(league, it)
        }

        verify(exactly = 1) {
            userClient.getByUsername(username)
            leagueClient.getByUserId(userResponse.userId)
        }
    }

    @MockBean(UserClient::class)
    fun userClient() = mockkClass(UserClient::class)

    @MockBean(LeagueClient::class)
    fun leagueClient() = mockkClass(LeagueClient::class)
}
