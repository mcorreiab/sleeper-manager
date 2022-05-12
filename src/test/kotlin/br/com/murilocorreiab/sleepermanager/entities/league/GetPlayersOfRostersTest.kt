package br.com.murilocorreiab.sleepermanager.entities.league

import br.com.murilocorreiab.sleepermanager.entities.league.model.Roster
import br.com.murilocorreiab.sleepermanager.entities.league.model.RosterFactory
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class GetPlayersOfRostersTest {

    @Test
    fun `get ids of players of rosters with success`() {
        // Arrange
        val roster1 = RosterFactory.build(players = listOf("1", "2"))
        val roster2 = RosterFactory.build(players = listOf("2", "3"))
        val roster3 = RosterFactory.build(players = listOf("3"))

        // Act
        val actual = listOf(roster1, roster2, roster3).getPlayersOnRosters()

        // Assert
        Assertions.assertThat(actual).containsExactlyInAnyOrder("1", "2", "3")
    }

    @ParameterizedTest
    @MethodSource("rosterWithoutPlayers")
    fun `if there's no rosters should return an empty list`(rosters: List<Roster>) {
        // Act
        val actual = rosters.getPlayersOnRosters()

        // Assert
        Assertions.assertThat(actual).isEmpty()
    }

    companion object {
        @JvmStatic
        fun rosterWithoutPlayers(): List<List<Roster>> = listOf(
            emptyList(),
            listOf(RosterFactory.build(players = emptyList())),
        )
    }
}
