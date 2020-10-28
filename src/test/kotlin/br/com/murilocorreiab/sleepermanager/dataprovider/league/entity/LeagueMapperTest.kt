package br.com.murilocorreiab.sleepermanager.dataprovider.league.entity

import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueMapper
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mapstruct.factory.Mappers

@MicronautTest
class LeagueMapperTest {

    private val target = Mappers.getMapper(LeagueMapper::class.java)

    @Test
    fun `should map league name with success`() {
        // Given
        val leagueName = "leagueName"
        val leagueResponse = LeagueResponseProducer(name = leagueName).build()

        // When
        val actual = target.convertToDomain(leagueResponse)

        // Then
        assertEquals(leagueName, actual.name)
    }

    @Test
    fun `should map league size with success`() {
        // Given
        val leagueSize = 16
        val leagueResponse = LeagueResponseProducer(totalRosters = leagueSize).build()

        // When
        val actual = target.convertToDomain(leagueResponse)

        // Then
        assertEquals(leagueSize, actual.size)
    }

    @Test
    fun `should map league id with success`() {
        // Given
        val leagueId = "38402"
        val leagueResponse = LeagueResponseProducer(leagueId = leagueId).build()

        // When
        val actual = target.convertToDomain(leagueResponse)

        // Then
        assertEquals(leagueId, actual.id)
    }
}