package br.com.murilocorreiab.sleepermanager.adapters.roster

import br.com.murilocorreiab.sleepermanager.adapters.league.LeagueExternalResponseMapper
import br.com.murilocorreiab.sleepermanager.entities.league.model.Roster
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(uses = [LeagueExternalResponseMapper::class])
interface RosterExternalResponseMapper {
    @Mappings(
        value = [
            Mapping(source = "rosterId", target = "id"),
            Mapping(
                target = "players",
                source = "players",
                defaultExpression = "java(new ArrayList<String>())",
            ),
        ],
    )
    fun toDomain(rosterResponse: RosterExternalResponse): Roster
}
