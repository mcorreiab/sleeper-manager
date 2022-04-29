package br.com.murilocorreiab.sleepermanager.entities.league

import br.com.murilocorreiab.sleepermanager.domain.league.entity.LeagueFactory
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class RemoveBestBallLeaguesTest {

    @Test
    fun `given a list of leagues should remove the best ball leagues`() {
        // Arrange
        val league1 = LeagueFactory.build(id = "1", isBestBall = false)
        val league2 = LeagueFactory.build(id = "2", isBestBall = false)
        val bestBallLeague = LeagueFactory.build(id = "3", isBestBall = true)

        // Act
        val actual = Leagues(listOf(league1, league2, bestBallLeague)).filterOutBestBallLeagues()

        // Assert
        Assertions.assertThat(actual).containsExactly(league1, league2)
    }

    @ParameterizedTest
    @MethodSource("noStandardLeagues")
    fun `if there's no standard leagues should return empty list`(leagues: List<League>) {
        // Arrange
        // Act
        val sut = Leagues(leagues)
        val actual = sut.filterOutBestBallLeagues()

        // Assert
        Assertions.assertThat(actual).isEmpty()
    }

    companion object {
        @JvmStatic
        fun noStandardLeagues(): List<List<League>> {
            return listOf(
                listOf(
                    LeagueFactory.build(id = "1", isBestBall = true),
                    LeagueFactory.build(id = "2", isBestBall = true),
                ),
                emptyList(),
            )
        }
    }
}
