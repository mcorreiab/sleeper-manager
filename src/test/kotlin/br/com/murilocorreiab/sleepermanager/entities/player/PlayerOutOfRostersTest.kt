package br.com.murilocorreiab.sleepermanager.entities.player

import br.com.murilocorreiab.sleepermanager.domain.league.entity.LeagueFactory
import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerFactory
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.RosterFactory2
import br.com.murilocorreiab.sleepermanager.entities.league.LeagueWithRosters
import br.com.murilocorreiab.sleepermanager.entities.league.model.LeaguesForPlayer
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class PlayerOutOfRostersTest {

    @Test
    fun `should get all players that are out of all rosters separated by league`() {
        // Arrange
        val playerNotRosteredRoster1 = PlayerFactory.build(id = "1", name = "aaron donald")
        val playerNotRosteredRoster2 = PlayerFactory.build(id = "2", name = "Russell")
        val playerNotRosteredBothRosters = PlayerFactory.build(id = "3", name = "RUSS")
        val playerAlwaysRostered = PlayerFactory.build(id = "4")
        val playerNameDoNotMatch = PlayerFactory.build(id = "5", name = "Jared")

        val roster1 = buildRoster("1", listOf(playerNotRosteredRoster2, playerAlwaysRostered))
        val roster2 = buildRoster("2", listOf(playerNotRosteredRoster1))
        val roster3 = buildRoster("3", listOf(playerAlwaysRostered, playerNameDoNotMatch))
        val league1Data = LeagueWithRosters(LeagueFactory.build(id = "1"), listOf(roster1))
        val league2Data = LeagueWithRosters(LeagueFactory.build(id = "2"), listOf(roster2, roster3))

        // Act
        val actual = FilterPlayers(
            listOf(
                playerNotRosteredRoster1,
                playerNotRosteredRoster2,
                playerNotRosteredBothRosters,
                playerAlwaysRostered,
            ),
            listOf("aaron", "russ"),
        ).filterOutOfRosters(listOf(league1Data, league2Data))

        // Assert
        Assertions.assertThat(actual).containsExactlyInAnyOrder(
            LeaguesForPlayer(playerNotRosteredRoster1, listOf(league1Data.league)),
            LeaguesForPlayer(playerNotRosteredRoster2, listOf(league2Data.league)),
            LeaguesForPlayer(playerNotRosteredBothRosters, listOf(league1Data.league, league2Data.league)),
        )
    }

    @ParameterizedTest
    @MethodSource("getEmptyListTestInputData")
    fun `when all players are rostered should return an empty list`(inputData: InputData) {
        // Act
        val actual = FilterPlayers(inputData.players, listOf("aaron", "russ")).filterOutOfRosters(inputData.leagues)

        // Assert
        Assertions.assertThat(actual).isEmpty()
    }

    companion object {
        private fun buildRoster(id: String, players: List<Player>) = RosterFactory2.build(
            id = id,
            players = players.map { RawPlayer(id = it.id) },
        )

        @JvmStatic
        fun getEmptyListTestInputData(): List<InputData> {
            val player1 = PlayerFactory.build(id = "1", name = "aaron donald")
            val player2 = PlayerFactory.build(id = "2", name = "Russell")
            val player3 = PlayerFactory.build(id = "3", name = "John")
            val roster = buildRoster("1", listOf(player1, player2))
            val leagueWithRosters = LeagueWithRosters(
                LeagueFactory.build(),
                listOf(roster),
            )

            return listOf(
                InputData(
                    listOf(player1, player2, player3),
                    listOf(leagueWithRosters),
                ),
                InputData(emptyList(), listOf(leagueWithRosters)),
                InputData(listOf(player1, player2, player3), emptyList()),
                InputData(emptyList(), emptyList()),
            )
        }

        data class InputData(
            val players: List<Player>,
            val leagues: List<LeagueWithRosters>,
        )
    }
}
