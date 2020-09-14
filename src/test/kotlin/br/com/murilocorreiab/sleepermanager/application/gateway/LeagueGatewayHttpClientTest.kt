package br.com.murilocorreiab.sleepermanager.application.gateway

import br.com.murilocorreiab.sleepermanager.application.http.league.LeagueClient
import br.com.murilocorreiab.sleepermanager.application.http.league.entity.LeagueMapper
import br.com.murilocorreiab.sleepermanager.application.http.league.entity.LeagueResponseProducer
import br.com.murilocorreiab.sleepermanager.application.http.user.UserClient
import br.com.murilocorreiab.sleepermanager.application.http.user.entity.UserResponseProducer
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

    @Inject
    private lateinit var leagueMapper: LeagueMapper

    @Test
    fun `should retrieve data with success`() {
        //Given
        val username = "username"
        val userResponse = UserResponseProducer().build()
        val leagueResponse = LeagueResponseProducer().build()
        val league = LeagueProducer().build()

        //When
        every { userClient.getByUsername(username) }.returns(Single.just(userResponse))
        every { leagueClient.getByUserId(userResponse.userId) }.returns(Flowable.just(leagueResponse))
        every { leagueMapper.convertToDomain(leagueResponse) }.returns(league)
        val actual = target.findUserLeagues(username)

        //Then
        assertEquals(arrayListOf(league), actual)
        verify(exactly = 1) {
            userClient.getByUsername(username)
            leagueClient.getByUserId(userResponse.userId)
            leagueMapper.convertToDomain(leagueResponse)
        }
    }

    @MockBean(UserClient::class)
    fun userClient() = mockkClass(UserClient::class)

    @MockBean(LeagueClient::class)
    fun leagueClient() = mockkClass(LeagueClient::class)

    @MockBean(LeagueMapper::class)
    fun leagueMapper() = mockkClass(LeagueMapper::class)
}