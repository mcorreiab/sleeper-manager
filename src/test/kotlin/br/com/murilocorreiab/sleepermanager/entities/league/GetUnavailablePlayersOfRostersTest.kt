package br.com.murilocorreiab.sleepermanager.entities.league

import br.com.murilocorreiab.sleepermanager.domain.league.entity.LeagueFactory
import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerFactory
import br.com.murilocorreiab.sleepermanager.entities.league.model.RosterFactory2
import br.com.murilocorreiab.sleepermanager.entities.league.model.RosterUnavailablePlayers
import br.com.murilocorreiab.sleepermanager.entities.player.PlayerStatus
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class GetUnavailablePlayersOfRostersTest {

    @TestAllPlayerStatusExcludingActive
    fun `should get unavailable starter players of a user with success`(playerStatus: PlayerStatus) {
        // Arrange
        val league = LeagueFactory.build(id = "5")
        val userId = "userId"

        val rosterWithInjuredPlayers =
            RosterFactory2.build(starters = listOf("1", "2", "3"), leagueId = league.id, ownerId = userId)
        val rosterWithActivePlayer = RosterFactory2.build(starters = listOf("3"), ownerId = userId)
        val rosterWithoutStarters = RosterFactory2.build(starters = emptyList(), ownerId = userId)
        val rosterOfAnotherUser = RosterFactory2.build(starters = listOf("1", "2"), ownerId = "otherUser")

        val injuredPlayer1 = PlayerFactory.build(id = "1", injuryStatus = playerStatus)
        val injuredPlayer2 = PlayerFactory.build(id = "2", injuryStatus = playerStatus)
        val activePlayer = PlayerFactory.build(id = "3", injuryStatus = PlayerStatus.ACTIVE)

        // Act
        val actual = RosterWithPlayers(
            listOf(rosterWithInjuredPlayers, rosterWithActivePlayer, rosterWithoutStarters, rosterOfAnotherUser),
            listOf(injuredPlayer1, injuredPlayer2, activePlayer),
            listOf(league),
            userId,
        ).getUnavailableStarters()

        // Assert
        val newRoster1 = RosterUnavailablePlayers(
            rosterWithInjuredPlayers.id,
            rosterWithInjuredPlayers.ownerId,
            listOf(injuredPlayer1, injuredPlayer2),
            league,
        )
        Assertions.assertThat(actual).containsExactly(newRoster1)
    }

    @TestAllPlayerStatusExcludingActive
    fun `if can't find unavailable starter players should return empty list`(playerStatus: PlayerStatus) {
        // Arrange
        val rosterWithInjuredPlayers = RosterFactory2.build(players = listOf("1"), starters = listOf("2"))

        val injuredPlayer = PlayerFactory.build(id = "1", injuryStatus = playerStatus)
        val activePlayer = PlayerFactory.build(id = "2", injuryStatus = PlayerStatus.ACTIVE)

        // Act
        val actual = RosterWithPlayers(
            listOf(rosterWithInjuredPlayers),
            listOf(injuredPlayer, activePlayer),
            emptyList(),
            "userId",
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
            emptyList(),
            "userId",
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
            emptyList(),
            "userId",
        ).getUnavailableStarters()

        // Assert
        Assertions.assertThat(actual).isEmpty()
    }

    @ParameterizedTest
    @EnumSource(mode = EnumSource.Mode.EXCLUDE, names = ["ACTIVE"])
    annotation class TestAllPlayerStatusExcludingActive
}
