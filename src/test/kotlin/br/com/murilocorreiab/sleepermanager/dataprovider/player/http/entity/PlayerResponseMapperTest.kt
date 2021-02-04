package br.com.murilocorreiab.sleepermanager.dataprovider.player.http.entity

import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.PlayerResponseMapper
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerStatus
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mapstruct.factory.Mappers

@MicronautTest
class PlayerResponseMapperTest {

    private val target = Mappers.getMapper(PlayerResponseMapper::class.java)

    @Test
    fun `should map with success`() {
        val playerResponse = PlayerResponseProducer().build()

        val expected = Player(
            id = playerResponse.playerId,
            name = playerResponse.fullName!!,
            starter = false,
            injuryStatus = playerResponse.injuryStatus!!,
            position = playerResponse.position,
            team = playerResponse.team
        )

        val actual = target.toDomain(playerResponse)

        assertEquals(expected, actual)
    }

    @Test
    fun `should map a list of players with success`() {
        val playerResponse = PlayerResponseProducer().build()
        assertTrue(target.toDomain(listOf(playerResponse)).isNotEmpty())
    }

    @Test
    fun `if injury status is missing should map to active`() {
        val playerWithoutInjury = PlayerResponseProducer(injuryStatus = null).build()
        val actualWithoutInjury = target.toDomain(playerWithoutInjury)
        assertEquals(PlayerStatus.ACTIVE.status, actualWithoutInjury.injuryStatus)
    }

    @Test
    fun `if full name is missing should map to first name + last name`() {
        val playerWithoutFullName = PlayerResponseProducer(fullName = null).build()
        val actualWithoutFullName = target.toDomain(playerWithoutFullName)
        assertEquals(
            "${playerWithoutFullName.firstName} ${playerWithoutFullName.lastName}",
            actualWithoutFullName.name
        )
    }
}
