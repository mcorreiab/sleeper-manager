package br.com.murilocorreiab.sleepermanager.adapters.roster

import br.com.murilocorreiab.sleepermanager.adapters.league.LeagueExternalResponseMapper
import br.com.murilocorreiab.sleepermanager.entities.league.model.Roster
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(uses = [LeagueExternalResponseMapper::class])
interface RosterExternalResponseMapper {
    @Mapping(source = "rosterId", target = "id")
    fun toDomain(rosterResponse: RosterExternalResponse): Roster
}
