package br.com.murilocorreiab.sleepermanager.dataprovider.http.league

import br.com.murilocorreiab.sleepermanager.dataprovider.http.league.entity.LeagueResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.http.user.UserClient
import br.com.murilocorreiab.sleepermanager.dataprovider.http.user.entity.UserResponseProducer
import br.com.murilocorreiab.sleepermanager.domain.entity.LeagueProducer
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.annotation.MockBean
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verify
import io.reactivex.Flowable
import io.reactivex.Single
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
    fun `should retrieve data with success`() {
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
        every { userClient.getByUsername(username) }.returns(Single.just(userResponse))
        every { leagueClient.getByUserId(userResponse.userId) }.returns(Flowable.just(leagueResponse))
        val actual = target.findUserLeagues(username)

        // Then
        assertEquals(arrayListOf(league), actual)
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
