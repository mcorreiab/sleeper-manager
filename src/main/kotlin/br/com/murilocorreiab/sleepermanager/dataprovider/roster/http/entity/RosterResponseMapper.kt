package br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity

import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueMapper
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueResponse
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(uses = [LeagueMapper::class])
interface RosterResponseMapper {
    @Mappings(
        value = [
            Mapping(source = "playerList", target = "players"),
            Mapping(source = "rosterResponse.rosterId", target = "id"),
            Mapping(source = "leagueResponse", target = "league")
        ]
    )
    fun toDomain(rosterResponse: RosterResponse, leagueResponse: LeagueResponse, playerList: List<Player>): Roster
}
