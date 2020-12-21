package br.com.murilocorreiab.sleepermanager.dataprovider.player

import br.com.murilocorreiab.sleepermanager.dataprovider.player.db.PlayerRepository
import br.com.murilocorreiab.sleepermanager.dataprovider.player.db.entity.PlayerDbProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.PlayerClient
import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.entity.PlayerResponseProducer
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class GetPlayersGatewayDataProviderTest {

    @Inject
    private lateinit var target: GetPlayersGatewayDataProvider

    @get:MockBean(PlayerRepository::class)
    val playerRepository = mockk<PlayerRepository>()

    @get:MockBean(PlayerClient::class)
    val playerClient = mockk<PlayerClient>()

    @ExperimentalCoroutinesApi
    @Test
    fun `should recover all informed players with success`() = runBlockingTest {
        // Given
        val firstNameToSearch = "Aaron"
        val secondNameToSearch = "Nelson"
        val player1 = PlayerDbProducer(id = "1", name = "Aaron Rodgers").build()
        val player2 = PlayerDbProducer(id = "2", name = "Aaron Jones").build()
        val player3 = PlayerDbProducer(id = "3", name = "Aaron Nelson").build()
        val player4 = PlayerDbProducer(id = "4", name = "Jordy Nelson").build()

        // When
        every { playerRepository.findByNameIlike("%$firstNameToSearch%") } returns listOf(
            player1,
            player2,
            player3
        )
        every { playerRepository.findByNameIlike("%$secondNameToSearch%") } returns listOf(
            player3,
            player4
        )

        val actual = target.getPlayersInformation(listOf(firstNameToSearch, secondNameToSearch)).toList()

        // Then
        assertEquals(4, actual.size)
        assertEquals(player1.id, actual[0].id)
        assertEquals(player2.id, actual[1].id)
        assertEquals(player3.id, actual[2].id)
        assertEquals(player4.id, actual[3].id)
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
        assertEquals(1, actual.size)
        assertNotNull(actual.first { it.id == player.playerId })
    }
}
