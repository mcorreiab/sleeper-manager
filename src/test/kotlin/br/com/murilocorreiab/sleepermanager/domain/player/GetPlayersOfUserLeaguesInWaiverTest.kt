package br.com.murilocorreiab.sleepermanager.domain.player

import br.com.murilocorreiab.sleepermanager.domain.league.entity.LeagueProducer
import br.com.murilocorreiab.sleepermanager.domain.league.gateway.LeagueGateway
import br.com.murilocorreiab.sleepermanager.domain.player.entity.LeaguesForPlayer
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerProducer
import br.com.murilocorreiab.sleepermanager.domain.player.gateway.PlayerGateway
import br.com.murilocorreiab.sleepermanager.domain.player.usecase.PlayersInWaiverUseCase
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster2
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.RosterFactory2
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
    fun `should list 2 players in waiver with success filtering out rostered players and bestball leagues`() {
        // Arrange
        val playerFullMatch = PlayerProducer.build(id = "1", name = "aaron donald")
        val playerPartialMatch = PlayerProducer.build(id = "2", name = "Russell")
        val playerAlwaysRostered = PlayerProducer.build(id = "3", name = "Aaron Jones")
        val playerNameDoNotMatch = PlayerProducer.build(id = "4", name = "Jared")

        val leagueBothPlayersAvailable = createLeagueWithBothPlayersAvailable(playerAlwaysRostered)
        val leagueOnlyFullMatchAvailable =
            createLeagueWithPlayerFullMatchAvailable(playerPartialMatch, playerAlwaysRostered)
        val leaguePlayerPartialMatchAvailable =
            createLeagueWithPlayerPartialMatchAvailable(playerFullMatch, playerAlwaysRostered)
        val bestBallLeague = createBestBallLeagueWithPlayerPartialMatchAvailable(playerFullMatch)

        val leagues = listOf(
            leagueBothPlayersAvailable,
            leagueOnlyFullMatchAvailable,
            leaguePlayerPartialMatchAvailable,
            bestBallLeague,
        )

        createMockForLeagues(leagues)
        createMockGetAllPlayers(
            listOf(
                playerFullMatch, playerPartialMatch, playerAlwaysRostered, playerNameDoNotMatch,
            ),
        )

        // Act
        val actual = sut.checkIfPlayersAreInWaiver(userId, listOf("aaron", "russ"))

        // Assert
        val expectFullMatchIn2Leagues = LeaguesForPlayer(
            player = playerFullMatch,
            leagues = listOf(leagueBothPlayersAvailable.build(), leagueOnlyFullMatchAvailable.build()),
        )
        val expectPartialMatchIn2Leagues = LeaguesForPlayer(
            player = playerPartialMatch,
            leagues = listOf(
                leagueBothPlayersAvailable.build(), leaguePlayerPartialMatchAvailable.build(),
            ),
        )

        Assertions.assertThat(actual).containsExactlyInAnyOrder(expectFullMatchIn2Leagues, expectPartialMatchIn2Leagues)
    }

    private fun createLeagueWithBothPlayersAvailable(playerAlwaysRostered: Player) =
        LeagueBuilder("1").withRoster("1").thatHavePlayer(playerAlwaysRostered.id).buildRoster()

    private fun createLeagueWithPlayerFullMatchAvailable(
        playerPartialMatch: Player,
        playerAlwaysRostered: Player
    ) = LeagueBuilder("2").withRoster("2").thatHavePlayer(playerPartialMatch.id).buildRoster().and().withRoster("3")
        .thatHavePlayer(playerAlwaysRostered.id).buildRoster()

    private fun createLeagueWithPlayerPartialMatchAvailable(
        playerFullMatch: Player,
        playerAlwaysRostered: Player
    ) = LeagueBuilder("3").withRoster("4").thatHavePlayer(playerFullMatch.id).thatHavePlayer(playerAlwaysRostered.id)
        .buildRoster()

    private fun createBestBallLeagueWithPlayerPartialMatchAvailable(playerFullMatch: Player) =
        LeagueBuilder("4").bestBall().withRoster("5").thatHavePlayer(playerFullMatch.id).buildRoster()

    @Test
    fun `should return an empty list if can't find players to list`() {
        // Arrange
        val rosteredPlayerInAllLeagues = PlayerProducer.build(id = "1", name = "bobby wagner")
        val playerDifferentName = PlayerProducer.build(id = "2", name = "Von")

        val league = LeagueBuilder("1").withRoster("1").thatHavePlayer(rosteredPlayerInAllLeagues.id).buildRoster()

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
        every { leagueGateway.findAllUserLeagues(userId) } returns leagues.map { it.build() }
        leagues.forEach {
            every { rosterGateway.findRostersOfLeague(it.build().id) } returns it.rosters
        }
    }

    private fun createMockGetAllPlayers(players: List<Player>) {
        every { playerGateway.getAllPlayers() } returns players
    }

    private class LeagueBuilder(
        private val id: String,
    ) {
        val rosters: MutableList<Roster2> = mutableListOf()

        private lateinit var rosterBuilder: RosterBuilder
        private var isBestBall = false

        fun bestBall() = apply {
            isBestBall = true
        }

        fun withRoster(id: String) = apply {
            rosterBuilder = RosterBuilder(id)
        }

        fun thatHavePlayer(id: String) = apply {
            rosterBuilder.withPlayer(id)
        }

        fun buildRoster() = apply {
            rosters.add(rosterBuilder.build())
        }

        fun and() = this

        fun build() = LeagueProducer.build(id = id, isBestBall = isBestBall)
    }

    private data class RosterBuilder(val id: String) {
        val players: MutableList<String> = mutableListOf()

        fun withPlayer(id: String) = players.add(id)

        fun build() = RosterFactory2.build(id = id, players = players)
    }

    @MockBean(LeagueGateway::class)
    fun LeagueGateway() = mockk<LeagueGateway>()

    @MockBean(RosterGateway2::class)
    fun RosterGateway2() = mockk<RosterGateway2>()

    @MockBean(PlayerGateway::class)
    fun PlayerGateway() = mockk<PlayerGateway>()
}
