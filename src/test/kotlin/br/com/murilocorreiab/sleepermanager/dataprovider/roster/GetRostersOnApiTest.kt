package br.com.murilocorreiab.sleepermanager.dataprovider.roster

import br.com.murilocorreiab.sleepermanager.adapters.roster.RosterClient
import br.com.murilocorreiab.sleepermanager.adapters.roster.RosterGatewayImpl
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.entity.RosterResponseFactory
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.entity.RosterResponseFactory.toDomain
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import jakarta.inject.Inject
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

@MicronautTest
class GetRostersOnApiTest {

    @Inject
    private lateinit var sut: RosterGatewayImpl

    @Inject
    private lateinit var rosterClient: RosterClient

    @Test
    fun `should get rosters of a league on api with success`() {
        // Arrange
        val leagueId = "leagueId"
        val roster1 = RosterResponseFactory.build(rosterId = "1")
        val roster2 = RosterResponseFactory.build(rosterId = "2", players = emptyList())
        every { rosterClient.getRostersOfALeague(leagueId) } returns listOf(
            roster1,
            roster2,
        )

        // Act
        val actual = sut.findRostersOfLeague(leagueId)

        // Assert
        Assertions.assertThat(actual).containsExactly(roster1.toDomain(), roster2.toDomain())
    }

    @MockBean(RosterClient::class)
    fun rosterClient() = mockk<RosterClient>()
}
