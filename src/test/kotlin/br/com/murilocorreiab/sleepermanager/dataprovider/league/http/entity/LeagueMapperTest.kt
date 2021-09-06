package br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity

import br.com.murilocorreiab.sleepermanager.domain.league.entity.League
import br.com.murilocorreiab.sleepermanager.domain.league.entity.PointsByReception
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mapstruct.factory.Mappers

@MicronautTest
class LeagueMapperTest {

    private val target = Mappers.getMapper(LeagueMapper::class.java)

    @Test
    fun `should map league with success`() {
        // Given
        val receptionPoints = 0.0

        val leagueResponse = LeagueResponseProducer.build(
            scoringSettingsResponse = ScoringSettingsResponseProducer.build(receptionPoints),
        )

        // When
        val expected = createExpectedLeague(leagueResponse, PointsByReception.STANDARD)
        val actual = target.convertToDomain(leagueResponse)

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `should map half ppr points by reception with success`() {
        // Given
        val receptionPoints = 0.5

        val leagueResponse = LeagueResponseProducer.build(
            scoringSettingsResponse = ScoringSettingsResponseProducer.build(receptionPoints),
        )

        // When
        val expected = createExpectedLeague(leagueResponse, PointsByReception.HALF_PPR)
        val actual = target.convertToDomain(leagueResponse)

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `should map full ppr points by reception with success`() {
        // Given
        val receptionPoints = 1.0

        val leagueResponse = LeagueResponseProducer.build(
            scoringSettingsResponse = ScoringSettingsResponseProducer.build(receptionPoints),
        )

        // When
        val expected = createExpectedLeague(leagueResponse, PointsByReception.PPR)
        val actual = target.convertToDomain(leagueResponse)

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `should map invalid points format to standard with success`() {
        // Given
        val receptionPoints = -1.0

        val leagueResponse = LeagueResponseProducer.build(
            scoringSettingsResponse = ScoringSettingsResponseProducer.build(receptionPoints),
        )

        // When
        val expected = createExpectedLeague(leagueResponse, PointsByReception.STANDARD)
        val actual = target.convertToDomain(leagueResponse)

        // Then
        assertEquals(expected, actual)
    }

    private fun createExpectedLeague(leagueResponse: LeagueResponse, pointsByReception: PointsByReception): League =
        League(
            name = leagueResponse.name,
            size = leagueResponse.totalRosters,
            id = leagueResponse.leagueId,
            pointsByReception = pointsByReception,
            avatar = leagueResponse.avatar
        )
}
