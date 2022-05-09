package br.com.murilocorreiab.sleepermanager.adapters.roster

import br.com.murilocorreiab.sleepermanager.adapters.league.LeagueResponseMapper
import br.com.murilocorreiab.sleepermanager.adapters.player.PlayerResponseMapper
import br.com.murilocorreiab.sleepermanager.entities.league.model.RosterUnavailablePlayers
import org.mapstruct.Mapper

@Mapper(uses = [LeagueResponseMapper::class, PlayerResponseMapper::class])
interface RosterResponseMapper {

    fun fromDomain(source: RosterUnavailablePlayers): RosterResponse
    fun fromDomain(source: List<RosterUnavailablePlayers>): List<RosterResponse>
}
