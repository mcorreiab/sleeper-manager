package br.com.murilocorreiab.sleepermanager.domain.roster

import br.com.murilocorreiab.sleepermanager.domain.roster.entity.RosterProducer
import br.com.murilocorreiab.sleepermanager.domain.roster.gateway.RosterGateway
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.coEvery
import io.mockk.mockkClass
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class ListUserRostersUseCaseTest {

    @Inject
    private lateinit var target: ListUserRostersUseCase

    @Inject
    private lateinit var rosterGateway: RosterGateway

    @Test
    fun `should list rosters of a user with success`() = runBlocking {
        // Given
        val userId = "userId"
        val leagueId = "leagueId"
        val roster = RosterProducer().build()

        // When
        coEvery { rosterGateway.findUserRosterInLeague(userId, leagueId) }.returns(roster)
        val actual = target.listUserRosterInLeague(userId, leagueId)

        // Then
        assertEquals(roster, actual)
    }

    @MockBean(RosterGateway::class)
    fun rosterGateway() = mockkClass(RosterGateway::class)
}
