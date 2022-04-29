package br.com.murilocorreiab.sleepermanager.entrypoint.entity

import br.com.murilocorreiab.sleepermanager.entities.league.League
import br.com.murilocorreiab.sleepermanager.entities.league.PointsByReception
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named

@Mapper(componentModel = "jsr330")
abstract class LeagueResponseMapper {

    @Mapping(source = "pointsByReception", target = "pointsByReception", qualifiedByName = ["mapPointsByReception"])
    abstract fun fromDomain(source: League): LeagueResponse

    @Named("mapPointsByReception")
    fun mapPointsByReception(source: PointsByReception) = source.text
}
