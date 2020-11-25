package br.com.murilocorreiab.sleepermanager.dataprovider.player

import br.com.murilocorreiab.sleepermanager.dataprovider.player.entity.PlayerResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.PlayerClient
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class PlayerGatewayHttpClientTest {

    @Inject
    private lateinit var target: PlayerGatewayHttpClient

    @get:MockBean(PlayerClient::class)
    val playerClient = mockk<PlayerClient>()

    @ExperimentalCoroutinesApi
    @Test
    fun `should recover all informed players with success`() = runBlockingTest {
        // Given
        val playerPartialName = "Aaron"
        val player1 = PlayerResponseProducer(playerId = "1", fullName = "Aaron Rodgers").build()
        val player2 = PlayerResponseProducer(playerId = "2", fullName = "Aaron Jones").build()
        val player3 = PlayerResponseProducer(playerId = "3", fullName = "Davante Adams").build()

        // When
        every { playerClient.getAllPlayers() }.returns(
            mapOf(
                player1.playerId to player1,
                player2.playerId to player2,
                player3.playerId to player3
            )
        )
        val actual = target.getPlayersInformation(listOf(playerPartialName)).toList()

        // Then
        assertEquals(2, actual.size)
        assertEquals(player1.playerId, actual[0].id)
        assertEquals(player2.playerId, actual[1].id)
    }
}
