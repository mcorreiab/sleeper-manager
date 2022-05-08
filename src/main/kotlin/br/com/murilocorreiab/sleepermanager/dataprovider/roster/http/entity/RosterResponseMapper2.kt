package br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity

import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueMapper
import br.com.murilocorreiab.sleepermanager.entities.league.model.Roster2
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(uses = [LeagueMapper::class])
interface RosterResponseMapper2 {
    @Mapping(source = "rosterId", target = "id")
    fun toDomain(rosterResponse: RosterResponse): Roster2
}
