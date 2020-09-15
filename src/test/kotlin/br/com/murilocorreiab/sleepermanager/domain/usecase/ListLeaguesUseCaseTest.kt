package br.com.murilocorreiab.sleepermanager.domain.usecase

import br.com.murilocorreiab.sleepermanager.domain.entity.LeagueProducer
import br.com.murilocorreiab.sleepermanager.domain.gateway.LeagueGateway
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.annotation.MockBean
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verify
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
    fun `should list leagues with success`() {
        //Given
        val username = "username"
        val id = 120L
        val name = "name"
        val size = 12
        val leagues = listOf(LeagueProducer(id = id, name = name, size = size).build())

        //When
        every { leagueGateway.findUserLeagues(username) }.returns(leagues)
        val actual = target.findUserLeagues(username)

        //Then
        assertEquals(leagues, actual)
        verify(exactly = 1) { leagueGateway.findUserLeagues(username) }
    }

    @MockBean(LeagueGateway::class)
    fun leagueGateway() = mockkClass(LeagueGateway::class)
}