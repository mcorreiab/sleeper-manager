package br.com.murilocorreiab.sleepermanager.entrypoint.entity

import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerProducer
import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerStatus
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mapstruct.factory.Mappers

@MicronautTest
class PlayerResponseMapperTest {

    private val target = Mappers.getMapper(PlayerResponseMapper::class.java)

    @Test
    fun `should map player with questionable status with success`() {
        checkMapping(PlayerStatus.QUESTIONABLE, PlayerStatus.QUESTIONABLE)
    }

    @Test
    fun `should map player with doubtful status with success`() {
        checkMapping(PlayerStatus.DOUBTFUL, PlayerStatus.DOUBTFUL)
    }

    @Test
    fun `should map player with IR status with success`() {
        checkMapping(PlayerStatus.IR, PlayerStatus.OUT)
    }

    private fun checkMapping(
        injuryStatus: PlayerStatus,
        expectedInjuryStatus: PlayerStatus
    ) {
        val player = PlayerProducer(injuryStatus = injuryStatus).build()

        val expected = PlayerResponse(
            id = player.id,
            name = player.name,
            injuryStatus = expectedInjuryStatus.status,
            starter = player.starter,
            position = player.position,
            team = player.team,
        )

        val actual = target.fromDomain(player)

        assertEquals(expected, actual)
    }
}