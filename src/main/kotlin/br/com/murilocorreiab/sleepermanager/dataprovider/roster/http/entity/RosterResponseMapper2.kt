package br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity

import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueMapper
import br.com.murilocorreiab.sleepermanager.domain.player.entity.RawPlayer
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster2
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.Named

@Mapper(uses = [LeagueMapper::class])
interface RosterResponseMapper2 {
    @Mappings(
        value = [
            Mapping(source = "rosterId", target = "id"),
            Mapping(
                source = "players",
                target = "players",
                qualifiedByName = ["mapPlayers"],
            ),
        ],
    )
    fun toDomain(rosterResponse: RosterResponse): Roster2

    @Named("mapPlayers")
    fun mapPlayers(source: List<String>?): List<RawPlayer> = source?.map {
        mapPlayer(it)
    } ?: emptyList()

    @Mapping(source = "source", target = "id")
    fun mapPlayer(source: String): RawPlayer
}
