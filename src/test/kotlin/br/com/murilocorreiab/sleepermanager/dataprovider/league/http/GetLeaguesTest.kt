package br.com.murilocorreiab.sleepermanager.dataprovider.league.http

import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.SettingsResponseProducer
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class GetLeaguesTest {

    @InjectMockKs
    private lateinit var target: GetLeagues

    @MockK
    private lateinit var leagueClient: LeagueClient

    @Test
    fun `should get rosters of a user filtering bestBall leagues with success`() {
        // Given
        val regularLeague = LeagueResponseProducer.build()
        val newRegularLeague = LeagueResponseProducer.build(settings = SettingsResponseProducer.build(bestBall = 0))
        val bestBallLeague = LeagueResponseProducer.build(settings = SettingsResponseProducer.build(bestBall = 1))
        val userId = "userId"

        // When
        every { leagueClient.getByUserId(userId) } returns listOf(regularLeague, newRegularLeague, bestBallLeague)

        val actual = target.getByUserId(userId)

        // Then
        Assertions.assertEquals(listOf(regularLeague, newRegularLeague), actual)
    }
}
