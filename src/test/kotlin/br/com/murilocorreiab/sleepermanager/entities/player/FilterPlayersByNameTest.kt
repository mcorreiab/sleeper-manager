package br.com.murilocorreiab.sleepermanager.entities.player

import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerFactory
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class FilterPlayersByNameTest {

    @Test
    fun `should filter players using the filters`() {
        // Arrange
        val playerFullMatch = PlayerFactory.build(id = "1", name = "aaron donald")
        val playerPartialMatch = PlayerFactory.build(id = "2", name = "Russell")
        val playerNameDoNotMatch = PlayerFactory.build(id = "4", name = "Jared")

        // Act
        val actual = Players(
            listOf(playerFullMatch, playerPartialMatch, playerNameDoNotMatch),
        ).filterByName(listOf("aaron", "russ"))

        // Assert
        Assertions.assertThat(actual).containsExactly(playerFullMatch, playerPartialMatch)
    }

    @Test
    fun `if there's no filters should return all players`() {
        // Arrange
        val player1 = PlayerFactory.build(id = "1")
        val player2 = PlayerFactory.build(id = "2")

        // Act
        val actual = Players(listOf(player1, player2)).filterByName(emptyList())

        // Assert
        Assertions.assertThat(actual).containsExactly(player1, player2)
    }

    @Test
    fun `if there's no players should return empty list`() {
        // Arrange

        // Act
        val actual = Players(emptyList()).filterByName(emptyList())

        // Assert
        Assertions.assertThat(actual).isEmpty()
    }
}
