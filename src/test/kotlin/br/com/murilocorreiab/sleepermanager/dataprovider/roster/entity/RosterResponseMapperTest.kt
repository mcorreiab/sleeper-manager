package br.com.murilocorreiab.sleepermanager.dataprovider.roster.entity

import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueResponseProducer
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.ScoringSettingsResponse
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.RosterResponseMapper
import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerFactory
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster
import br.com.murilocorreiab.sleepermanager.entities.league.League
import br.com.murilocorreiab.sleepermanager.entities.league.PointsByReception
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mapstruct.factory.Mappers

@MicronautTest
class RosterResponseMapperTest {

    private val target = Mappers.getMapper(RosterResponseMapper::class.java)

    @Test
    fun `should map roster response with success`() {
        val pointsByReception = 0.0
        val players = listOf(PlayerFactory.build())
        val rosterResponse = RosterResponseFactory.build()
        val leagueResponse =
            LeagueResponseProducer.build(scoringSettingsResponse = ScoringSettingsResponse(pointsByReception))

        val league = League(
            name = leagueResponse.name,
            id = leagueResponse.leagueId,
            size = leagueResponse.totalRosters,
            avatar = leagueResponse.avatar,
            pointsByReception = PointsByReception.STANDARD,
            isBestBall = false,
        )
        val expected =
            Roster(id = rosterResponse.rosterId, ownerId = rosterResponse.ownerId, players = players, league = league)

        val actual = target.toDomain(rosterResponse, leagueResponse, players)

        assertEquals(expected, actual)
    }
}
