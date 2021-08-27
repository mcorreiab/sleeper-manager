package br.com.murilocorreiab.sleepermanager.domain.player.usecase

import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerProducer
import br.com.murilocorreiab.sleepermanager.domain.player.gateway.GetPlayersGateway
import br.com.murilocorreiab.sleepermanager.domain.player.gateway.ModifyPlayerGateway
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class UpdatePlayerUseCaseTest {

    @InjectMockKs
    private lateinit var target: UpdatePlayerUseCase

    @MockK
    private lateinit var playerGateway: GetPlayersGateway

    @MockK
    private lateinit var modifyPlayerGateway: ModifyPlayerGateway

    @Test
    fun `should update players with success`() {
        // Given
        val playersToUpdate = listOf(PlayerProducer.build())

        // When
        every { playerGateway.getAllPlayers() } returns playersToUpdate
        every { modifyPlayerGateway.updatePlayers(playersToUpdate) } returns playersToUpdate
        target.updatePlayers()

        // Then
        verify(exactly = 1) { modifyPlayerGateway.updatePlayers(playersToUpdate) }
    }
}
