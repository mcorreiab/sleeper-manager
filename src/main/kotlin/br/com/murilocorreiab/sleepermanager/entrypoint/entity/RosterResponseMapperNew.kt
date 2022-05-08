package br.com.murilocorreiab.sleepermanager.entrypoint.entity

import br.com.murilocorreiab.sleepermanager.entities.league.model.RosterUnavailablePlayers
import org.mapstruct.Mapper

@Mapper(uses = [LeagueResponseMapper::class, PlayerResponseMapper::class])
interface RosterResponseMapperNew { // TODO: Rename to RosterResponseMapper

    fun fromDomain(source: RosterUnavailablePlayers): RosterResponse
    fun fromDomain(source: List<RosterUnavailablePlayers>): List<RosterResponse>
}
