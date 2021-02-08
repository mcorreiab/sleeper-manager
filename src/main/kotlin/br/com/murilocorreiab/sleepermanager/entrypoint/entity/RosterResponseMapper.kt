package br.com.murilocorreiab.sleepermanager.entrypoint.entity

import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster
import org.mapstruct.Mapper

@Mapper(uses = [LeagueResponseMapper::class])
interface RosterResponseMapper {

    fun fromDomain(source: Roster): RosterResponse
    fun fromDomain(source: List<Roster>): List<RosterResponse>
}
