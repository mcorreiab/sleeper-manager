package br.com.murilocorreiab.sleepermanager.dataprovider.player

import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.GetPlayers
import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerProducer
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class GetPlayersGatewayDataProviderTest {

    @InjectMockKs
    private lateinit var target: GetPlayersGatewayDataProvider

    @MockK
    private lateinit var getPlayers: GetPlayers

    @Test
    fun `should recover all informed players with success`() {
        // Given
        val firstNameToSearch = "Aaron"
        val secondNameToSearch = "nelson"
        val player1 = PlayerProducer.build(id = "1", name = "Aaron Rodgers")
        val player2 = PlayerProducer.build(id = "2", name = "Aaron Jones")
        val player3 = PlayerProducer.build(id = "3", name = "David Bakhtiari")
        val player4 = PlayerProducer.build(id = "4", name = "Jordy Nelson")

        // When
        every { getPlayers.getAllPlayers() } returns listOf(player1, player2, player3, player4)

        val actual = target.getPlayersInformation(listOf(firstNameToSearch, secondNameToSearch)).toList()

        // Then
        assertEquals(3, actual.size)
        assertEquals(player1.id, actual[0].id)
        assertEquals(player2.id, actual[1].id)
        assertEquals(player4.id, actual[2].id)
    }

    @Test
    fun `should get all players with success`() {
        // Given
        val player = PlayerProducer.build()

        // When
        every { getPlayers.getAllPlayers() } returns listOf(player)
        val actual = target.getAllPlayers()

        // Then
        assertEquals(1, actual.size)
        assertNotNull(actual.first { it.id == player.id })
    }
}
