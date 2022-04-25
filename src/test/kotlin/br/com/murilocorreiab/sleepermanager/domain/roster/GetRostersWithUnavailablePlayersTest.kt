package br.com.murilocorreiab.sleepermanager.domain.roster

import br.com.murilocorreiab.sleepermanager.domain.league.gateway.LeagueGateway
import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerStatus
import br.com.murilocorreiab.sleepermanager.domain.player.gateway.PlayerGateway
import br.com.murilocorreiab.sleepermanager.domain.roster.gateway.RosterGateway2
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import jakarta.inject.Inject
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

@MicronautTest
class GetRostersWithUnavailablePlayersTest {

    @Inject
    private lateinit var sut: RostersUnavailablePlayersUseCase

    @Inject
    private lateinit var leagueGateway: LeagueGateway

    @Inject
    private lateinit var rosterGateway: RosterGateway2

    @Inject
    private lateinit var playerGateway: PlayerGateway

    private val userId = "userId"

    @ParameterizedTest
    @EnumSource(mode = EnumSource.Mode.EXCLUDE, names = ["ACTIVE"])
    fun `should get all rosters with unavailable players in starting slot with success`(playerStatus: PlayerStatus) {
        // Arrange
        val unavailablePlayer =
            PlayerMockFactory(id = "1", playerStatus = playerStatus, playerGateway = playerGateway).get()
        val unavailable = PlayerMockFactory(id = "2", playerStatus = playerStatus, playerGateway = playerGateway).get()
        val activePlayer =
            PlayerMockFactory(id = "3", playerStatus = PlayerStatus.ACTIVE, playerGateway = playerGateway).get()

        val leagueMockFactory = LeagueMockFactory(
            id = "1",
            rosterGateway = rosterGateway,
        ).newRoster(
            LeagueMockFactory.NewRosterData(
                "roster1",
                listOf(unavailablePlayer, unavailable, activePlayer),
                userId,
                listOf(unavailablePlayer.id),
            ),
        ).newRoster(
            LeagueMockFactory.NewRosterData(
                "roster2",
                listOf(unavailablePlayer),
                "otherUser",
                emptyList(),
            ),
        ).create()
        val league1 = leagueMockFactory.getLeague()

        val league2 = LeagueMockFactory(id = "2", rosterGateway = rosterGateway).newRoster(
            LeagueMockFactory.NewRosterData(
                "roster3",
                listOf(activePlayer),
                userId,
                emptyList(),
            ),
        ).create().getLeague()
        every { leagueGateway.findAllUserLeagues(userId) } returns listOf(
            league1,
            league2,
        )

        // Act
        val actual = sut.getByUserId(userId)

        // Assert
        val expectedRoster = leagueMockFactory.getRosterById("roster1").copy(players = listOf(unavailablePlayer))
        Assertions.assertThat(actual).containsExactly(expectedRoster)
    }

    @Test
    @Disabled
    fun `if cant find any roster with unavailable players should return an empty list`() {
        // Arrange
        // TODO: Finish this test

        // Act
        val actual = sut.getByUserId(userId)

        // Assert
        Assertions.assertThat(actual).isEmpty()
    }

    @Test
    fun `if cant find any roster should return an empty list`() {
    }

    @MockBean(LeagueGateway::class)
    fun leagueGateway() = mockk<LeagueGateway>()

    @MockBean(RosterGateway2::class)
    fun rosterGateway() = mockk<RosterGateway2>()

    @MockBean(PlayerGateway::class)
    fun playerGateway() = mockk<PlayerGateway>()
}
