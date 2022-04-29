package br.com.murilocorreiab.sleepermanager.dataprovider.player.http

import br.com.murilocorreiab.sleepermanager.dataprovider.player.db.PlayerRepositoryImpl
import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.entity.PlayerResponseProducer
import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerFactory
import br.com.murilocorreiab.sleepermanager.entities.player.PlayerStatus
import br.com.murilocorreiab.sleepermanager.entities.player.Team
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class GetPlayersTest {

    @InjectMockKs
    lateinit var target: GetPlayers

    @MockK
    lateinit var playerClient: PlayerClient

    @MockK
    lateinit var playerRepository: PlayerRepositoryImpl

    @Test
    fun `should get all players from remote api`() {
        // Given
        val playerResponse = PlayerResponseProducer.build(team = Team.GB.name, injuryStatus = PlayerStatus.OUT.status)
        val player = PlayerFactory.build(
            id = playerResponse.playerId,
            name = playerResponse.fullName!!,
            injuryStatus = PlayerStatus.OUT,
            starter = false,
            position = playerResponse.position!!,
            team = Team.GB,
        )

        // When
        every { playerRepository.getAll() } returns emptyList()
        every { playerClient.getAllPlayers() } returns mapOf(playerResponse.playerId to playerResponse)
        every { playerRepository.create(listOf(player)) } returns Unit
        val actual = target.getAllPlayers()

        // Then
        Assertions.assertEquals(1, actual.size)
        Assertions.assertNotNull(actual.first { it.id == playerResponse.playerId })
        verify { playerRepository.create(listOf(player)) }
    }

    @Test
    fun `should get all players from database`() {
        // Given
        val player = PlayerFactory.build()

        // When
        every { playerRepository.getAll() } returns listOf(player)
        val actual = target.getAllPlayers()

        // Then
        Assertions.assertEquals(1, actual.size)
        Assertions.assertEquals(actual.first(), player)
    }

    @Test
    fun `when cant find any player return empty list`() {
        // Given
        // When
        every { playerRepository.getAll() } returns emptyList()
        every { playerClient.getAllPlayers() } returns emptyMap()
        every { playerRepository.create(emptyList()) } returns Unit
        val actual = target.getAllPlayers()

        // Then
        Assertions.assertTrue(actual.isEmpty())
    }

    @Test
    fun `should find player by id in database`() {
        // Given
        val player = PlayerFactory.build()

        // When
        every { playerRepository.getById(player.id) } returns player
        val actual = target.getPlayerById(player.id)

        // Then
        Assertions.assertEquals(actual, player)
    }

    @Test
    fun `if cant find player a by id in database should find in external api`() {
        // Given
        val playerResponse = PlayerResponseProducer.build(team = Team.GB.name, injuryStatus = PlayerStatus.OUT.status)
        val player = PlayerFactory.build(
            id = playerResponse.playerId,
            name = playerResponse.fullName!!,
            injuryStatus = PlayerStatus.OUT,
            starter = false,
            position = playerResponse.position!!,
            team = Team.GB,
        )

        // When
        every { playerRepository.getById(player.id) } returns null
        every { playerRepository.getAll() } returns emptyList()
        every { playerClient.getAllPlayers() } returns mapOf(playerResponse.playerId to playerResponse)
        every { playerRepository.create(listOf(player)) } returns Unit

        val actual = target.getPlayerById(player.id)

        // Then
        Assertions.assertEquals(actual, player)
    }

    @Test
    fun `if cant find player neither in external api or database should return null`() {
        // Given
        val id = "id"

        // When
        every { playerRepository.getById(id) } returns null
        every { playerRepository.getAll() } returns emptyList()
        every { playerClient.getAllPlayers() } returns emptyMap()
        every { playerRepository.create(emptyList()) } returns Unit

        val actual = target.getPlayerById(id)

        // Then
        Assertions.assertNull(actual)
    }
}
