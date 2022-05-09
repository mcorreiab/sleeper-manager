package br.com.murilocorreiab.sleepermanager.dataprovider.league

import br.com.murilocorreiab.sleepermanager.adapters.league.LeagueGatewayImpl
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueResponseProducer.toDomain
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.ScoringSettingsResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.SettingsResponseProducer
import br.com.murilocorreiab.sleepermanager.entities.league.model.PointsByReception
import br.com.murilocorreiab.sleepermanager.framework.LeagueClient
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import jakarta.inject.Inject
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

@MicronautTest
class GetAllLeaguesTest {

    @Inject
    private lateinit var sut: LeagueGatewayImpl

    @Inject
    private lateinit var leagueClient: LeagueClient

    @Test
    fun `should get all leagues of user id with success`() {
        // Arrange
        val standardScoringLeague = LeagueResponseProducer.build(
            leagueId = "1",
            scoringSettingsResponse = ScoringSettingsResponseProducer.build(rec = 0.0),
        )
        val halfPprLeague = LeagueResponseProducer.build(
            leagueId = "2",
            scoringSettingsResponse = ScoringSettingsResponseProducer.build(rec = 0.5),
            settings = SettingsResponseProducer.build(bestBall = 1),
        )
        val fullPprLeague = LeagueResponseProducer.build(
            leagueId = "3",
            scoringSettingsResponse = ScoringSettingsResponseProducer.build(rec = 1.0),
            settings = SettingsResponseProducer.build(bestBall = 0),
        )
        val invalidScoringLeague = LeagueResponseProducer.build(
            leagueId = "4",
            scoringSettingsResponse = ScoringSettingsResponseProducer.build(rec = -1.0),
        )

        every { leagueClient.getByUserId("userId") } returns listOf(
            standardScoringLeague,
            halfPprLeague,
            fullPprLeague,
            invalidScoringLeague,
        )

        // Act
        val result = sut.findAllUserLeagues("userId")

        // Assert
        Assertions.assertThat(result).containsExactly(
            standardScoringLeague.toDomain(PointsByReception.STANDARD),
            halfPprLeague.toDomain(PointsByReception.HALF_PPR),
            fullPprLeague.toDomain(PointsByReception.PPR),
            invalidScoringLeague.toDomain(PointsByReception.STANDARD),
        )
    }

    @MockBean(LeagueClient::class)
    fun leagueClient() = mockk<LeagueClient>()
}
