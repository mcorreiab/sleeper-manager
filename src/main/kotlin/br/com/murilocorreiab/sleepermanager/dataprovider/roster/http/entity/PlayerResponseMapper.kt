package br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity

import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Player
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.PlayerStatus
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.Named

@Mapper
interface PlayerResponseMapper {

    @Mappings(
        value = [
            Mapping(
                target = "name",
                expression = "java(playerResponse.getFirstName() + \" \" + playerResponse.getLastName())"
            ),
            Mapping(target = "isStarter", source = "playerId", qualifiedByName = ["toIsStarter"]),
            Mapping(target = "id", source = "playerId"),
            Mapping(target = "injuryStatus", source = "injuryStatus", qualifiedByName = ["toInjuryStatus"])
        ]
    )
    fun toDomain(playerResponse: PlayerResponse, @Context starters: List<String>): Player

    fun toDomain(playerResponse: List<PlayerResponse>, @Context starters: List<String>): List<Player>

    companion object {
        @Named("toIsStarter")
        @JvmStatic
        fun toIsStarter(playerId: String, @Context starters: List<String>): Boolean = starters.contains(playerId)

        @Named("toInjuryStatus")
        @JvmStatic
        fun toInjuryStatus(injuryStatus: String) = PlayerStatus.parsePlayerStatus(injuryStatus)
    }
}
