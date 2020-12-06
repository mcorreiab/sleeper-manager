package br.com.murilocorreiab.sleepermanager.dataprovider.player

import br.com.murilocorreiab.sleepermanager.dataprovider.player.entity.PlayerResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.PlayerClient
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class GetPlayersGatewayHttpClientTest {

    @Inject
    private lateinit var target: GetPlayersGatewayHttpClient

    @get:MockBean(PlayerClient::class)
    val playerClient = mockk<PlayerClient>()

    @ExperimentalCoroutinesApi
    @Test
    fun `should recover all informed players with success`() = runBlockingTest {
        // Given
        val firstNameToSearch = "Aaron"
        val secondNameToSearch = "Nelson"
        val player1 = PlayerResponseProducer(playerId = "1", fullName = "Aaron Rodgers").build()
        val player2 =
            PlayerResponseProducer(playerId = "2", fullName = null, firstName = "Aaron", lastName = "Jones").build()
        val player3 = PlayerResponseProducer(playerId = "3", fullName = "Davante Adams").build()
        val player4 = PlayerResponseProducer(playerId = "4", fullName = "Jordy Nelson").build()

        // When
        every { playerClient.getAllPlayers() }.returns(
            mapOf(
                player1.playerId to player1,
                player2.playerId to player2,
                player3.playerId to player3,
                player4.playerId to player4
            )
        )
        val actual = target.getPlayersInformation(listOf(firstNameToSearch, secondNameToSearch)).toList()

        // Then
        assertEquals(3, actual.size)
        assertEquals(player1.playerId, actual[0].id)
        assertEquals(player2.playerId, actual[1].id)
        assertEquals(player4.playerId, actual[2].id)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should get all players with success`() = runBlockingTest {
        // Given
        val player = PlayerResponseProducer().build()

        // When
        every { playerClient.getAllPlayers() } returns mapOf(player.playerId to player)
        val actual = target.getAllPlayers()

        // Then
        assertEquals(1, actual.count())
        assertNotNull(actual.first { it.id == player.playerId })
    }
}
