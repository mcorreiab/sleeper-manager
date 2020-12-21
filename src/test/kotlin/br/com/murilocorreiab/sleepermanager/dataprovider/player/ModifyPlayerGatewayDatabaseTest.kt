package br.com.murilocorreiab.sleepermanager.dataprovider.player

import br.com.murilocorreiab.sleepermanager.dataprovider.player.db.PlayerRepository
import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerProducer
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class ModifyPlayerGatewayDatabaseTest {

    @Inject
    lateinit var target: ModifyPlayerGatewayDatabase

    @Inject
    lateinit var playerRepository: PlayerRepository

    @Test
    fun `should save all players`() {
        val playerToSave = PlayerProducer().build()
        runBlocking {
            target.updatePlayers(listOf(playerToSave)).toList()
        }

        val player = playerRepository.findById(playerToSave.id)
        assertTrue(player.isPresent)
    }
}
