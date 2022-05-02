package br.com.murilocorreiab.sleepermanager.entities.league

import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerFactory
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.RosterFactory2
import br.com.murilocorreiab.sleepermanager.entities.player.PlayerStatus
import br.com.murilocorreiab.sleepermanager.entities.player.RawPlayer
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class GetUnavailablePlayersOfRostersTest {

    @TestAllPlayerStatusExcludingActive
    fun `should get unavailable starter players with success`(playerStatus: PlayerStatus) {
        // Arrange
        val rosterWithInjuredPlayers = RosterFactory2.build(starters = listOf("1", "2", "3"))
        val rosterWithActivePlayer = RosterFactory2.build(starters = listOf("3"))
        val rosterWithoutStarters = RosterFactory2.build(starters = emptyList())

        val injuredPlayer1 = PlayerFactory.build(id = "1", injuryStatus = playerStatus)
        val injuredPlayer2 = PlayerFactory.build(id = "2", injuryStatus = playerStatus)
        val activePlayer = PlayerFactory.build(id = "3", injuryStatus = PlayerStatus.ACTIVE)

        // Act
        val actual = RosterWithPlayers(
            listOf(rosterWithInjuredPlayers, rosterWithActivePlayer, rosterWithoutStarters),
            listOf(injuredPlayer1, injuredPlayer2, activePlayer),
        ).getUnavailableStarters()

        // Assert
        val newRoster1 = rosterWithInjuredPlayers.copy(players = listOf(injuredPlayer1, injuredPlayer2))
        Assertions.assertThat(actual).containsExactly(newRoster1)
    }

    @TestAllPlayerStatusExcludingActive
    fun `if can't find unavailable starter players should return empty list`(playerStatus: PlayerStatus) {
        // Arrange
        val rosterWithInjuredPlayers = RosterFactory2.build(players = listOf(RawPlayer("1")), starters = listOf("2"))

        val injuredPlayer = PlayerFactory.build(id = "1", injuryStatus = playerStatus)
        val activePlayer = PlayerFactory.build(id = "2", injuryStatus = PlayerStatus.ACTIVE)

        // Act
        val actual = RosterWithPlayers(
            listOf(rosterWithInjuredPlayers),
            listOf(injuredPlayer, activePlayer),
        ).getUnavailableStarters()

        // Assert
        Assertions.assertThat(actual).isEmpty()
    }

    @Test
    fun `if there's no players should return empty list`() {
        // Act
        val actual = RosterWithPlayers(
            listOf(RosterFactory2.build(starters = listOf("1", "2", "3"))),
            emptyList(),
        ).getUnavailableStarters()

        // Assert
        Assertions.assertThat(actual).isEmpty()
    }

    @Test
    fun `if there's no rosters should return empty list`() {
        // Act
        val actual = RosterWithPlayers(
            emptyList(),
            listOf(PlayerFactory.build(injuryStatus = PlayerStatus.OUT)),
        ).getUnavailableStarters()

        // Assert
        Assertions.assertThat(actual).isEmpty()
    }

    @ParameterizedTest
    @EnumSource(mode = EnumSource.Mode.EXCLUDE, names = ["ACTIVE"])
    annotation class TestAllPlayerStatusExcludingActive
}
