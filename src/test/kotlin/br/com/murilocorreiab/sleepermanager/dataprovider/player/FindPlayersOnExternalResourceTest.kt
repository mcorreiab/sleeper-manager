package br.com.murilocorreiab.sleepermanager.dataprovider.player

import br.com.murilocorreiab.sleepermanager.dataprovider.player.db.PlayerRepository
import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.PlayerClient
import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.entity.PlayerResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.entity.PlayerResponseProducer.toDomain
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerFactory
import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerStatus
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Team
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import jakarta.inject.Inject
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

@MicronautTest
class FindPlayersOnExternalResourceTest {

    @Inject
    private lateinit var sut: PlayerGatewayImpl

    @Inject
    private lateinit var playerClient: PlayerClient

    @Inject
    private lateinit var playerRepository: PlayerRepository

    private val playerFieldsFilled = PlayerResponseProducer.build(
        playerId = "1", injuryStatus = PlayerStatus.ACTIVE.status, team = Team.GB.name,
    )
    private val playerOptionalFieldsMissing = PlayerResponseProducer.build(
        playerId = "2",
        injuryStatus = null,
        team = null,
        fullName = null,
        position = null,
    )
    private val playerEmptyInjuryStatus = PlayerResponseProducer.build(playerId = "3", injuryStatus = "")

    private val playerFieldsFilledDomain = playerFieldsFilled.toDomain(
        name = playerFieldsFilled.fullName!!,
        position = playerFieldsFilled.position!!,
        injuryStatus = PlayerStatus.ACTIVE,
        team = Team.GB,
        starter = false,
    )

    private val playerOptionalFieldsMissingDomain = playerOptionalFieldsMissing.toDomain(
        name = "${playerOptionalFieldsMissing.firstName} ${playerOptionalFieldsMissing.lastName}",
        position = "No position",
        injuryStatus = PlayerStatus.ACTIVE,
        team = Team.NO_TEAM,
        starter = false,
    )

    private val playerEmptyInjuryStatusDomain = playerEmptyInjuryStatus.toDomain(
        name = playerEmptyInjuryStatus.fullName!!,
        position = playerEmptyInjuryStatus.position!!,
        injuryStatus = PlayerStatus.ACTIVE,
        team = Team.GB,
        starter = false,
    )

    @Test
    fun `should get all players from api with success`() {
        // Arrange
        every { playerRepository.getAll() } returns emptyList()
        every { playerClient.getAllPlayers() } returns mapOf(
            playerFieldsFilled.playerId to playerFieldsFilled,
            playerOptionalFieldsMissing.playerId to playerOptionalFieldsMissing,
            playerEmptyInjuryStatus.playerId to playerEmptyInjuryStatus,
        )

        // Act
        val actual = sut.getAllPlayers()

        // Assert
        assertThatFoundAllPlayers(actual)
    }

    @Test
    fun `should get all players from repository with success`() {
        // Arrange
        every { playerRepository.getAll() } returns listOf(
            playerFieldsFilledDomain,
            playerOptionalFieldsMissingDomain,
            playerEmptyInjuryStatusDomain,
        )

        // Act
        val actual = sut.getAllPlayers()

        // Assert
        assertThatFoundAllPlayers(actual)
    }

    private fun assertThatFoundAllPlayers(actual: List<Player>) {
        Assertions.assertThat(actual)
            .containsExactly(playerFieldsFilledDomain, playerOptionalFieldsMissingDomain, playerEmptyInjuryStatusDomain)
    }

    @Test
    fun `should get a player by his ID directly from repository with success`() {
        // Arrange
        every { playerRepository.getById(playerFieldsFilled.playerId) } returns playerFieldsFilledDomain

        // Act
        val actual = sut.getById(playerFieldsFilled.playerId)

        // Assert
        Assertions.assertThat(actual).isEqualTo(playerFieldsFilledDomain)
    }

    @Test
    fun `should get a player by his ID from api when not in repository with success`() {
        // Arrange
        every { playerRepository.getById(playerFieldsFilled.playerId) } returns null
        every { playerRepository.getAll() } returns listOf(playerFieldsFilledDomain)

        // Act
        val actual = sut.getById(playerFieldsFilled.playerId)

        // Assert
        Assertions.assertThat(actual).isEqualTo(playerFieldsFilledDomain)
    }

    @ParameterizedTest
    @MethodSource("playerByIdMissingProvider")
    fun `if can't find a player neither on repository or api should return null`(players: List<Player>) {
        // Arrange
        every { playerRepository.getById(playerFieldsFilled.playerId) } returns null
        every { playerRepository.getAll() } returns players
        every { playerClient.getAllPlayers() } returns emptyMap()

        // Act
        val actual = sut.getById(playerFieldsFilled.playerId)

        // Assert
        Assertions.assertThat(actual).isNull()
    }

    companion object {
        @JvmStatic
        fun playerByIdMissingProvider() = listOf(emptyList(), listOf(PlayerFactory.build()))
    }

    @MockBean(PlayerClient::class)
    fun playerClient() = mockk<PlayerClient>()

    @MockBean(PlayerRepository::class)
    fun playerRepository() = mockk<PlayerRepository>(relaxed = true)
}
