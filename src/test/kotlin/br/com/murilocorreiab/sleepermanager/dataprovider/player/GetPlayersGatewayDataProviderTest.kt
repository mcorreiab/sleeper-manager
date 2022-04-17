package br.com.murilocorreiab.sleepermanager.dataprovider.player

import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.GetPlayers
import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerFactory
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
    fun `should get all players with success`() {
        // Given
        val player = PlayerFactory.build()

        // When
        every { getPlayers.getAllPlayers() } returns listOf(player)
        val actual = target.getAllPlayers()

        // Then
        assertEquals(1, actual.size)
        assertNotNull(actual.first { it.id == player.id })
    }
}
