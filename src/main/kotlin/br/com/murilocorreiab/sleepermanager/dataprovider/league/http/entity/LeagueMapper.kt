package br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity

import br.com.murilocorreiab.sleepermanager.domain.league.entity.League
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(componentModel = "jsr330")
interface LeagueMapper {

    @Mappings(
        value = [
            Mapping(source = "leagueId", target = "id"),
            Mapping(source = "totalRosters", target = "size")
        ]
    )
    fun convertToDomain(leagueResponse: LeagueResponse): League
}
