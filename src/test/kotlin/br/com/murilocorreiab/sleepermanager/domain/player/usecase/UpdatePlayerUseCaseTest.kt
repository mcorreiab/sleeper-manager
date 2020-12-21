package br.com.murilocorreiab.sleepermanager.domain.player.usecase

import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerProducer
import br.com.murilocorreiab.sleepermanager.domain.player.gateway.GetPlayersGateway
import br.com.murilocorreiab.sleepermanager.domain.player.gateway.ModifyPlayerGateway
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class UpdatePlayerUseCaseTest {

    @Inject
    lateinit var target: UpdatePlayerUseCase

    @get:MockBean(GetPlayersGateway::class)
    val playerGateway = mockk<GetPlayersGateway>()

    @get:MockBean(ModifyPlayerGateway::class)
    val modifyPlayerGateway = mockk<ModifyPlayerGateway>()

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
