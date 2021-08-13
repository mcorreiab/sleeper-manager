package br.com.murilocorreiab.sleepermanager.entrypoint.entity

import br.com.murilocorreiab.sleepermanager.domain.roster.entity.RosterProducer
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mapstruct.factory.Mappers

@MicronautTest
class RosterResponseMapperTest {

    private val target = Mappers.getMapper(RosterResponseMapper::class.java)

    @Test
    fun `should map roster response with success`() {
        val roster = RosterProducer().build()
        val league = roster.league
        val expectedLeague = LeagueResponse(
            name = league.name,
            id = league.id,
            size = league.size,
            avatar = league.avatar,
            pointsByReception = league.pointsByReception.text
        )

        val players = roster.players.map {
            PlayerResponse(
                id = it.id,
                name = it.name,
                injuryStatus = it.injuryStatus,
                starter = it.starter,
                position = it.position,
                team = it.team,
            )
        }

        val expected =
            RosterResponse(id = roster.id, ownerId = roster.ownerId, players = players, league = expectedLeague)

        val actual = target.fromDomain(roster)

        assertEquals(expected, actual)
    }

    @Test
    fun `should map a list of rosters with success`() {
        val roster = RosterProducer().build()

        val actual = target.fromDomain(listOf(roster))

        assertTrue(actual.isNotEmpty())
    }
}
