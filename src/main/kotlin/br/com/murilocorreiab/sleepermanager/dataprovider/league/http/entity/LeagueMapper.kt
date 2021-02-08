package br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity

import br.com.murilocorreiab.sleepermanager.domain.league.entity.League
import br.com.murilocorreiab.sleepermanager.domain.league.entity.PointsByReception
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.Named

@Mapper(componentModel = "jsr330")
abstract class LeagueMapper {

    @Mappings(
        value = [
            Mapping(source = "leagueId", target = "id"),
            Mapping(source = "totalRosters", target = "size"),
            Mapping(
                source = "scoringSettings.rec",
                target = "pointsByReception",
                qualifiedByName = ["convertPointsByReception"]
            )
        ]
    )
    abstract fun convertToDomain(leagueResponse: LeagueResponse): League

    @Named("convertPointsByReception")
    fun convertPointsByReception(pointsByReception: Double) = PointsByReception.getByPoints(pointsByReception)
}
