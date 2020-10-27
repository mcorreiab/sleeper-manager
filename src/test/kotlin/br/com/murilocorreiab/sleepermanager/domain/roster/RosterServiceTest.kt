package br.com.murilocorreiab.sleepermanager.domain.roster

import br.com.murilocorreiab.sleepermanager.domain.roster.entity.PlayerProducer
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.PlayerStatus
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.RosterProducer
import br.com.murilocorreiab.sleepermanager.domain.roster.gateway.RosterGateway
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.coEvery
import io.mockk.mockkClass
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class RosterServiceTest {

    @Inject
    private lateinit var target: RosterService

    @Inject
    private lateinit var rosterGateway: RosterGateway

    @Test
    fun `should return all doubtful players in starter lineup with success`() = runBlocking {
        // Given
        val username = "username"
        val expectedRosterId = "roster1"
        val availablePlayer = PlayerProducer(id = "player1", name = "available", PlayerStatus.ACTIVE).build()
        val outPlayer = PlayerProducer(id = "player2", name = "out", PlayerStatus.OUT).build()
        val irPlayer = PlayerProducer(id = "player3", name = "ir", PlayerStatus.IR).build()
        val outPlayerInBench = PlayerProducer(id = "player4", name = "out", PlayerStatus.OUT, isStarter = false).build()
        val roster =
            RosterProducer(
                id = expectedRosterId,
                players = listOf(availablePlayer, outPlayer, irPlayer, outPlayerInBench)
            ).build()
        val rosterWithOnlyActivePlayer =
            RosterProducer(id = "roster2", players = listOf(availablePlayer, outPlayerInBench)).build()
        val expected = RosterProducer(id = expectedRosterId, players = listOf(outPlayer, irPlayer)).build()

        // When
        coEvery { rosterGateway.findUserRostersInLeagues(username) }.returns(flowOf(roster, rosterWithOnlyActivePlayer))
        val actual = target.checkForUnavailablePlayers(username)

        // Then
        actual.collect { assertEquals(expected, it) }
    }

    @MockBean(RosterGateway::class)
    fun rosterGateway() = mockkClass(RosterGateway::class)
}
