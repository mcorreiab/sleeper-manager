package br.com.murilocorreiab.sleepermanager.dataprovider.player.db.entity

import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerProducer
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mapstruct.factory.Mappers

@MicronautTest
class PlayerDbMapperTest {

    private val target = Mappers.getMapper(PlayerDbMapper::class.java)

    @Test
    fun `should map player from domain with success`() {
        // Given
        val playerId = "playerId"
        val injuryStatus = "injuryStatus"
        val name = "name"
        val starter = false
        val position = "position"
        val team = "team"
        val player =
            Player(
                id = playerId,
                injuryStatus = injuryStatus,
                name = name,
                starter = starter,
                position = position,
                team = team
            )
        val expected =
            PlayerDb(id = playerId, injuryStatus = injuryStatus, name = name, position = position, team = team)

        // When
        val actual = target.fromDomain(player)

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `should map a list of players with success`() {
        // Given
        val player = PlayerProducer().build()

        // When
        val actual = target.fromDomain(listOf(player))

        // Then
        assertTrue(actual.isNotEmpty())
    }

    @Test
    fun `should map player to domain with success`() {
        // Given
        val playerId = "playerId"
        val injuryStatus = "injuryStatus"
        val name = "name"
        val starter = false
        val position = "position"
        val team = "team"
        val player = PlayerDb(id = playerId, injuryStatus = injuryStatus, name = name, position = position, team = team)
        val expected =
            Player(
                id = playerId,
                injuryStatus = injuryStatus,
                name = name,
                starter = starter,
                position = position,
                team = team
            )

        // When
        val actual = target.toDomain(player)

        // Then
        assertEquals(expected, actual)
    }
}
