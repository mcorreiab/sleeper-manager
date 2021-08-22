package br.com.murilocorreiab.sleepermanager.dataprovider.player.http

import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Team
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
abstract class PlayerResponseMapper {

    @Mappings(
        value = [
            Mapping(
                target = "name",
                expression = "java(mapName(playerResponse.getFullName()," + " playerResponse.getFirstName()," +
                    " playerResponse.getLastName()))"
            ),
            Mapping(target = "starter", constant = "false"),
            Mapping(target = "id", source = "playerId"),
            Mapping(
                target = "injuryStatus",
                source = "injuryStatus",
                defaultValue = "Active"
            ),
            Mapping(target = "team", source = "team", defaultValue = "NO_TEAM"),
            Mapping(target = "position", source = "position", defaultValue = "No position")
        ]
    )
    abstract fun toDomain(playerResponse: PlayerResponse): Player

    abstract fun toDomain(playerResponse: List<PlayerResponse>): List<Player>

    fun mapName(fullName: String?, firstName: String, lastName: String) = fullName ?: "$firstName $lastName"
}
