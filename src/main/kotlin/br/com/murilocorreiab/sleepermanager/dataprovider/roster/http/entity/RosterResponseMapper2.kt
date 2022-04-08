package br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity

import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueMapper
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster2
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(uses = [LeagueMapper::class])
interface RosterResponseMapper2 {
    @Mappings(
        value = [
            Mapping(source = "rosterId", target = "id"),
            Mapping(
                source = "players",
                target = "players",
                defaultExpression = "java(new ArrayList<>())",
            ),
        ],
    )
    fun toDomain(rosterResponse: RosterResponse): Roster2
}
