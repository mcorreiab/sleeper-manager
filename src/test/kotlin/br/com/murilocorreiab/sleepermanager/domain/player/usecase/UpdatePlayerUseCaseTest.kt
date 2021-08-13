package br.com.murilocorreiab.sleepermanager.domain.player.usecase

import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerProducer
import br.com.murilocorreiab.sleepermanager.domain.player.gateway.GetPlayersGateway
import br.com.murilocorreiab.sleepermanager.domain.player.gateway.ModifyPlayerGateway
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
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

    @ExperimentalCoroutinesApi
    @Test
    fun `should update players with success`() = runBlockingTest {
        // Given
        val playersToUpdate = listOf(PlayerProducer().build())

        // When
        coEvery { playerGateway.getAllPlayers() } returns playersToUpdate
        coEvery { modifyPlayerGateway.updatePlayers(playersToUpdate) } returns playersToUpdate
        target.updatePlayers()

        // Then
        coVerify(exactly = 1) { modifyPlayerGateway.updatePlayers(playersToUpdate) }
    }
}
