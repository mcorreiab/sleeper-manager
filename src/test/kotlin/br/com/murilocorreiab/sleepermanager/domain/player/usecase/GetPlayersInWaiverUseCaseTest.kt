package br.com.murilocorreiab.sleepermanager.domain.player.usecase

import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerProducer
import br.com.murilocorreiab.sleepermanager.domain.player.gateway.PlayerGateway
import br.com.murilocorreiab.sleepermanager.domain.roster.gateway.RosterGateway
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class GetPlayersInWaiverUseCaseTest {

    @Inject
    lateinit var target: GetPlayersInWaiverUseCase

    @get:MockBean(RosterGateway::class)
    val rosterGateway = mockk<RosterGateway>()

    @get:MockBean(PlayerGateway::class)
    val playerGateway = mockk<PlayerGateway>()

    @ExperimentalCoroutinesApi
    @Test
    fun `should list players in waiver with success`() = runBlockingTest {
        // Given
        val playerInWaiverName = "player1 available"
        val playerInRosterName = "player2 unavailable"
        val playerInWaiver = PlayerProducer(name = playerInWaiverName, id = playerInWaiverName).build()
        val playerInRoster = PlayerProducer(name = playerInRosterName, id = playerInRosterName).build()
        val playersToCheck = listOf(playerInWaiverName, playerInRosterName)
        val username = "username"

        // When
        coEvery { rosterGateway.findAllRosteredPlayersInUserLeagues(username) }.returns(flowOf(playerInRoster))
        coEvery { playerGateway.getPlayersInformation(playersToCheck) }.returns(flowOf(playerInWaiver, playerInRoster))
        val actual = target.get(username, playersToCheck).toList()

        // Then
        assertEquals(1, actual.size)
        assertEquals(playerInWaiver.id, actual[0].id)
    }
}
