package br.com.murilocorreiab.sleepermanager.dataprovider.roster.entity

import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.PlayerResponseMapper
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.PlayerStatus
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mapstruct.factory.Mappers

@MicronautTest
class PlayerResponseMapperTest {

    private val starterId = "starterId"
    private val target = Mappers.getMapper(PlayerResponseMapper::class.java)
    private val playerResponse =
        PlayerResponseProducer(playerId = starterId, injuryStatus = PlayerStatus.IR.name).build()
    private val starters = listOf(starterId)
    private val actual = target.toDomain(playerResponse, starters)
    private val benchPlayer = target.toDomain(PlayerResponseProducer(playerId = "otherPlayer").build(), starters)

    @Test
    fun `should map id with success`() {
        assertEquals(playerResponse.playerId, actual.id)
    }

    @Test
    fun `should map name with success`() {
        assertEquals("${playerResponse.firstName} ${playerResponse.lastName}", actual.name)
    }

    @Test
    fun `should map injury status with success`() {
        assertEquals(PlayerStatus.IR, actual.injuryStatus)
    }

    @Test
    fun `should map if is starter with success`() {
        assertTrue(actual.isStarter)
    }

    @Test
    fun `should map if is not a starter with success`() {
        assertFalse(benchPlayer.isStarter)
    }

    @Test
    fun `should map a list of players with success`() {
        val actualList = target.toDomain(listOf(playerResponse), starters)
        assertTrue(actualList.isNotEmpty())
    }
}
