package br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity

import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.Named

@Mapper
abstract class PlayerResponseMapper {

    @Mappings(
        value = [
            Mapping(
                target = "name",
                expression = "java(mapName(playerResponse.getFullName()," + " playerResponse.getFirstName()," +
                    " playerResponse.getLastName()))"
            ),
            Mapping(target = "isStarter", source = "playerId", qualifiedByName = ["toIsStarter"]),
            Mapping(target = "id", source = "playerId"),
            Mapping(
                target = "injuryStatus",
                source = "injuryStatus",
                defaultValue = "Active"
            )
        ]
    )
    abstract fun toDomain(playerResponse: PlayerResponse, @Context starters: List<String>): Player

    abstract fun toDomain(playerResponse: List<PlayerResponse>, @Context starters: List<String>): List<Player>

    @Named("toIsStarter")
    fun toIsStarter(playerId: String, @Context starters: List<String>): Boolean = starters.contains(playerId)

    fun mapName(fullName: String?, firstName: String, lastName: String) = fullName ?: "$firstName $lastName"
}
