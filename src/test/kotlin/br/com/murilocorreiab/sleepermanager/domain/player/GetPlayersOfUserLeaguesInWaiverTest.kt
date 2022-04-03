package br.com.murilocorreiab.sleepermanager.domain.player

import br.com.murilocorreiab.sleepermanager.domain.league.entity.League
import br.com.murilocorreiab.sleepermanager.domain.league.entity.LeagueProducer
import br.com.murilocorreiab.sleepermanager.domain.league.gateway.LeagueGateway
import br.com.murilocorreiab.sleepermanager.domain.player.entity.LeaguesForPlayer
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerProducer
import br.com.murilocorreiab.sleepermanager.domain.player.gateway.PlayerGateway
import br.com.murilocorreiab.sleepermanager.domain.player.usecase.PlayersInWaiverUseCase
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster2
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.RosterProducer2
import br.com.murilocorreiab.sleepermanager.domain.roster.gateway.RosterGateway2
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import jakarta.inject.Inject
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

@MicronautTest
class GetPlayersOfUserLeaguesInWaiverTest {

    @Inject
    private lateinit var sut: PlayersInWaiverUseCase

    @Inject
    private lateinit var leagueGateway: LeagueGateway

    @Inject
    private lateinit var rosterGateway: RosterGateway2

    @Inject
    private lateinit var playerGateway: PlayerGateway

    private val userId = "userId"

    @Test
    fun `should list 2 players in waiver with success filtering out rostered players`() {
        // Arrange
        val playerFullMatch = PlayerProducer.build(id = "1", name = "aaron donald")
        val playerPartialMatch = PlayerProducer.build(id = "2", name = "Russell")
        val playerNotInWaivers = PlayerProducer.build(id = "3", name = "Aaron Jones")
        val playerDifferentName = PlayerProducer.build(id = "4", name = "Jared")

        val league1 = LeagueBuilder("1").withRoster("1").thatHavePlayer(playerNotInWaivers.id).completeRoster()
        val league2 = LeagueBuilder("2").withRoster("2").thatHavePlayer(playerPartialMatch.id).completeRoster().and()
            .withRoster("3").thatHavePlayer(playerNotInWaivers.id).completeRoster()
        val league3 =
            LeagueBuilder("3").withRoster("4").thatHavePlayer(playerFullMatch.id).thatHavePlayer(playerNotInWaivers.id)
                .completeRoster()

        val leagues = listOf(league1, league2, league3)

        createMockForLeagues(leagues)
        createMockGetAllPlayers(
            listOf(
                playerFullMatch, playerPartialMatch, playerNotInWaivers, playerDifferentName
            )
        )

        // Act
        val actual = sut.checkIfPlayersAreInWaiver(userId, listOf("aaron", "russ"))

        // Assert
        val expectFullMatchIn2Leagues = LeaguesForPlayer(
            player = playerFullMatch, leagues = listOf(league1.league, league2.league)
        )
        val expectPartialMatchIn2Leagues = LeaguesForPlayer(
            player = playerPartialMatch, leagues = listOf(league1.league, league3.league)
        )

        Assertions.assertThat(actual).containsExactlyInAnyOrder(expectFullMatchIn2Leagues, expectPartialMatchIn2Leagues)
    }

    @Test
    fun `should return an empty list if can't find players to list`() {
        // Arrange
        val rosteredPlayerInAllLeagues = PlayerProducer.build(id = "1", name = "bobby wagner")
        val playerDifferentName = PlayerProducer.build(id = "2", name = "Von")

        val league = LeagueBuilder("1").withRoster("1").thatHavePlayer(rosteredPlayerInAllLeagues.id).completeRoster()

        createMockForLeagues(listOf(league))
        createMockGetAllPlayers(listOf(rosteredPlayerInAllLeagues, playerDifferentName))

        // Act
        val actual = sut.checkIfPlayersAreInWaiver(userId, listOf("bobby wagner"))

        // Assert
        Assertions.assertThat(actual).isEmpty()
    }

    @Test
    fun `if user doesn't have leagues should return empty list`() {
        // Arrange
        createMockForLeagues(emptyList())
        createMockGetAllPlayers(listOf(PlayerProducer.build()))

        // Act
        val actual = sut.checkIfPlayersAreInWaiver(userId, listOf("bobby wagner"))

        // Assert
        Assertions.assertThat(actual).isEmpty()
    }

    @Test
    fun `if user doesn't have leagues and can't find any player should return empty list`() {
        // Arrange
        createMockForLeagues(emptyList())
        createMockGetAllPlayers(emptyList())

        // Act
        val actual = sut.checkIfPlayersAreInWaiver(userId, listOf("bobby wagner"))

        // Assert
        Assertions.assertThat(actual).isEmpty()
    }

    private fun createMockForLeagues(leagues: List<LeagueBuilder>) {
        every { leagueGateway.findAllUserLeagues(userId) } returns leagues.map { it.league }
        leagues.forEach {
            every { rosterGateway.findRostersOfLeague(it.league.id) } returns it.rosters
        }
    }

    private fun createMockGetAllPlayers(players: List<Player>) {
        every { playerGateway.getAllPlayers() } returns players
    }

    private class LeagueBuilder(
        id: String,
    ) {
        val league: League = LeagueProducer.build(id = id)
        val rosters: MutableList<Roster2> = mutableListOf()

        private lateinit var rosterBuilder: RosterBuilder
        fun withRoster(id: String) = apply {
            rosterBuilder = RosterBuilder(id)
        }

        fun thatHavePlayer(id: String) = apply {
            rosterBuilder.withPlayer(id)
        }

        fun completeRoster() = apply {
            rosters.add(rosterBuilder.build())
        }

        fun and() = this
    }

    private data class RosterBuilder(val id: String) {
        val players: MutableList<String> = mutableListOf()

        fun withPlayer(id: String) = players.add(id)

        fun build() = RosterProducer2.build(id = id, players = players)
    }

    @MockBean(LeagueGateway::class)
    fun LeagueGateway() = mockk<LeagueGateway>()

    @MockBean(RosterGateway2::class)
    fun RosterGateway2() = mockk<RosterGateway2>()

    @MockBean(PlayerGateway::class)
    fun PlayerGateway() = mockk<PlayerGateway>()
}
