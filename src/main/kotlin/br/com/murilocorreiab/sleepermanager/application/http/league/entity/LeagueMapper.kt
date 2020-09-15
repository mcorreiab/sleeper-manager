package br.com.murilocorreiab.sleepermanager.application.http.league.entity

import br.com.murilocorreiab.sleepermanager.domain.entity.League
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(componentModel = "jsr330")
interface LeagueMapper {

    @Mappings(value = [
        Mapping(source = "leagueId", target = "id"),
        Mapping(source = "totalRosters", target = "size")]
    )
    fun convertToDomain(leagueResponse: LeagueResponse): League
}