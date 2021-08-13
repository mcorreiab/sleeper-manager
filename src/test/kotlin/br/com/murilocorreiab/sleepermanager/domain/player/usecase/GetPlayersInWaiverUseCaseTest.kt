package br.com.murilocorreiab.sleepermanager.domain.player.usecase

import br.com.murilocorreiab.sleepermanager.domain.league.entity.LeagueProducer
import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerProducer
import br.com.murilocorreiab.sleepermanager.domain.player.gateway.GetPlayersGateway
import br.com.murilocorreiab.sleepermanager.domain.roster.gateway.RosterGateway
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class GetPlayersInWaiverUseCaseTest {

    @InjectMockKs
    private lateinit var target: GetPlayersInWaiverUseCase

    @MockK
    private lateinit var rosterGateway: RosterGateway

    @MockK
    private lateinit var playerGateway: GetPlayersGateway

    @ExperimentalCoroutinesApi
    @Test
    fun `should list players in waiver with success`() = runBlockingTest {
        // Given
        val playerInWaiverName = "player1 available"
        val playerInRosterName = "player2 unavailable"
        val playerInWaiver = PlayerProducer(name = playerInWaiverName, id = playerInWaiverName).build()
        val playerInRoster = PlayerProducer(name = playerInRosterName, id = playerInRosterName).build()
        val playersToCheck = listOf(playerInWaiverName, playerInRosterName)
        val leagueWithPlayerAvailable = LeagueProducer(id = "league1").build()
        val leagueWithPlayerRostered = LeagueProducer(id = "league2").build()
        val username = "username"

        // When
        coEvery { rosterGateway.findAllRosteredPlayersInUserLeagues(username) }.returns(
            listOf(
                leagueWithPlayerAvailable to listOf(
                    playerInRoster
                ),
                leagueWithPlayerRostered to listOf(playerInWaiver, playerInRoster)
            )
        )
        coEvery { playerGateway.getPlayersInformation(playersToCheck) }.returns(listOf(playerInWaiver, playerInRoster))
        val actual = target.get(username, playersToCheck).toList().toMap()

        // Then
        val matchedLeagues = actual[playerInWaiver]?.toList()
        assertEquals(1, actual.keys.size)
        assertEquals(playerInWaiver, actual.keys.toList()[0])
        assertEquals(1, matchedLeagues?.size)
        assertEquals(leagueWithPlayerAvailable, matchedLeagues?.get(0))
    }
}
