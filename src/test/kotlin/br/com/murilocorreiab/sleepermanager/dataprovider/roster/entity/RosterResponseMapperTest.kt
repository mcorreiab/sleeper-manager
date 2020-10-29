package br.com.murilocorreiab.sleepermanager.dataprovider.roster.entity

import br.com.murilocorreiab.sleepermanager.dataprovider.league.entity.LeagueResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.RosterResponseMapper
import br.com.murilocorreiab.sleepermanager.domain.league.entity.League
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.PlayerProducer
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mapstruct.factory.Mappers

@MicronautTest
class RosterResponseMapperTest {

    private val target = Mappers.getMapper(RosterResponseMapper::class.java)
    private val rosterResponse = RosterResponseProducer().build()
    private val leagueResponse = LeagueResponseProducer().build()
    private val players = listOf(PlayerProducer().build())
    private val actual = target.toDomain(rosterResponse, leagueResponse, players)

    @Test
    fun `should map id with success`() {
        assertEquals(rosterResponse.rosterId, actual.id)
    }

    @Test
    fun `should map ownerId with success`() {
        assertEquals(rosterResponse.ownerId, actual.ownerId)
    }

    @Test
    fun `should map league with success`() {
        val league =
            League(name = leagueResponse.name, id = leagueResponse.leagueId, size = leagueResponse.totalRosters)
        assertEquals(league, actual.league)
    }

    @Test
    fun `should map players with success`() {
        assertEquals(players, actual.players)
    }
}
