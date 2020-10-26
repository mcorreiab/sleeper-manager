package br.com.murilocorreiab.sleepermanager.domain.league

import br.com.murilocorreiab.sleepermanager.domain.league.entity.LeagueProducer
import br.com.murilocorreiab.sleepermanager.domain.league.gateway.LeagueGateway
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkClass
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class ListLeaguesUseCaseTest {

    @Inject
    private lateinit var target: ListLeaguesUseCase

    @Inject
    private lateinit var leagueGateway: LeagueGateway

    @Test
    fun `should list leagues with success`() = runBlocking {
        // Given
        val username = "username"
        val id = 120L
        val name = "name"
        val size = 12
        val leagues = flowOf(LeagueProducer(id = id, name = name, size = size).build())

        // When
        coEvery { leagueGateway.findUserLeagues(username) }.returns(leagues)
        val actual = target.findUserLeagues(username)

        // Then
        assertEquals(leagues, actual)
        coVerify(exactly = 1) { leagueGateway.findUserLeagues(username) }
    }

    @MockBean(LeagueGateway::class)
    fun leagueGateway() = mockkClass(LeagueGateway::class)
}
