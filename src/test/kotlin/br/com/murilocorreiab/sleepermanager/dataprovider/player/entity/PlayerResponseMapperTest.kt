package br.com.murilocorreiab.sleepermanager.dataprovider.player.entity

import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.PlayerResponseMapper
import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerStatus
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mapstruct.factory.Mappers

@MicronautTest
class PlayerResponseMapperTest {

    private val target = Mappers.getMapper(PlayerResponseMapper::class.java)
    private val playerResponse =
        PlayerResponseProducer(injuryStatus = PlayerStatus.IR.status).build()
    private val actual = target.toDomain(playerResponse)

    @Test
    fun `should map id with success`() {
        assertEquals(playerResponse.playerId, actual.id)
    }

    @Test
    fun `should map name with success`() {
        assertEquals(playerResponse.fullName, actual.name)
    }

    @Test
    fun `should map injury status with success`() {
        assertEquals(PlayerStatus.IR.status, actual.injuryStatus)
    }

    @Test
    fun `should have starter default value as false`() {
        assertFalse(actual.starter)
    }

    @Test
    fun `should map a list of players with success`() {
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
