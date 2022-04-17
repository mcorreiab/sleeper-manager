package br.com.murilocorreiab.sleepermanager.domain.roster.usecase

import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerFactory
import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerStatus
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.RosterProducer
import br.com.murilocorreiab.sleepermanager.domain.roster.gateway.RosterGateway
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class GetRostersWithUnavailablePlayersUseCaseTest {

    @InjectMockKs
    private lateinit var target: GetRostersWithUnavailablePlayersUseCase

    @MockK
    private lateinit var rosterGateway: RosterGateway

    private val expectedRosterId = "roster1"
    private val availablePlayer = PlayerFactory.build(id = "player1", name = "available", PlayerStatus.ACTIVE)
    private val outPlayer = PlayerFactory.build(id = "player2", name = "out", PlayerStatus.OUT)
    private val irPlayer = PlayerFactory.build(id = "player3", name = "ir", PlayerStatus.OUT)
    private val outPlayerInBench =
        PlayerFactory.build(id = "player4", name = "out", PlayerStatus.OUT, starter = false)
    private val roster =
        RosterProducer.build(
            id = expectedRosterId,
            players = listOf(availablePlayer, outPlayer, irPlayer, outPlayerInBench)
        )
    private val rosterWithOnlyActivePlayer =
        RosterProducer.build(id = "roster2", players = listOf(availablePlayer, outPlayerInBench))
    private val expected = RosterProducer.build(id = expectedRosterId, players = listOf(outPlayer, irPlayer))

    @Test
    fun `should return all doubtful players in starter lineup with success`() {
        // Given
        val username = "username"

        // When
        every { rosterGateway.findUserRostersByUsernameInLeagues(username) } returns
            listOf(roster, rosterWithOnlyActivePlayer)
        val actual = target.getByUsername(username)

        // Then
        actual.forEach { assertEquals(expected, it) }
    }

    @Test
    fun `should return all doubtful players by user id`() {
        // Given
        val userId = "userId"
        // When
        every { rosterGateway.findUserRostersByUserIdInLeagues(userId) } returns
            listOf(roster, rosterWithOnlyActivePlayer)
        val actual = target.getByUserId(userId)

        // Then
        actual.forEach { assertEquals(expected, it) }
    }
}
