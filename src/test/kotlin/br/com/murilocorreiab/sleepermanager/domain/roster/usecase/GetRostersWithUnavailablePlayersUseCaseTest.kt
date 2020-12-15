package br.com.murilocorreiab.sleepermanager.domain.roster.usecase

import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerProducer
import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerStatus
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.RosterProducer
import br.com.murilocorreiab.sleepermanager.domain.roster.gateway.RosterGateway
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class GetRostersWithUnavailablePlayersUseCaseTest {

    @Inject
    private lateinit var target: GetRostersWithUnavailablePlayersUseCase

    @get:MockBean(RosterGateway::class)
    val rosterGateway = mockk<RosterGateway>()

    @ExperimentalCoroutinesApi
    @Test
    fun `should return all doubtful players in starter lineup with success`() = runBlockingTest {
        // Given
        val username = "username"
        val expectedRosterId = "roster1"
        val availablePlayer = PlayerProducer(id = "player1", name = "available", PlayerStatus.ACTIVE.status).build()
        val outPlayer = PlayerProducer(id = "player2", name = "out", PlayerStatus.OUT.status).build()
        val irPlayer = PlayerProducer(id = "player3", name = "ir", PlayerStatus.OUT.status).build()
        val outPlayerInBench =
            PlayerProducer(id = "player4", name = "out", PlayerStatus.OUT.status, starter = false).build()
        val roster =
            RosterProducer(
                id = expectedRosterId,
                players = listOf(availablePlayer, outPlayer, irPlayer, outPlayerInBench)
            ).build()
        val rosterWithOnlyActivePlayer =
            RosterProducer(id = "roster2", players = listOf(availablePlayer, outPlayerInBench)).build()
        val expected = RosterProducer(id = expectedRosterId, players = listOf(outPlayer, irPlayer)).build()

        // When
        coEvery { rosterGateway.findUserRostersInLeagues(username) }.returns(listOf(roster, rosterWithOnlyActivePlayer))
        val actual = target.get(username)

        // Then
        actual.forEach { assertEquals(expected, it) }
    }
}
