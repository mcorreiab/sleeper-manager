package br.com.murilocorreiab.sleepermanager.entrypoint.entity

import br.com.murilocorreiab.sleepermanager.domain.league.entity.LeagueProducer
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mapstruct.factory.Mappers

@MicronautTest
class LeagueResponseMapperTest {

    private val target = Mappers.getMapper(LeagueResponseMapper::class.java)

    @Test
    fun `should map from domain with success`() {
        val league = LeagueProducer().build()

        val expected = LeagueResponse(
            name = league.name,
            id = league.id,
            size = league.size,
            avatar = league.avatar,
            pointsByReception = league.pointsByReception.text
        )

        val actual = target.fromDomain(league)

        assertEquals(expected, actual)
    }
}
