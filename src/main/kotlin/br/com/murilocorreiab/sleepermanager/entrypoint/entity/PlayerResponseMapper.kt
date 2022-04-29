package br.com.murilocorreiab.sleepermanager.entrypoint.entity

import br.com.murilocorreiab.sleepermanager.entities.player.Player
import br.com.murilocorreiab.sleepermanager.entities.player.PlayerStatus
import br.com.murilocorreiab.sleepermanager.entities.player.Team
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named

@Mapper
abstract class PlayerResponseMapper {

    @Mapping(target = "injuryStatus", source = "injuryStatus", qualifiedByName = ["mapInjuryStatus"])
    abstract fun fromDomain(source: Player): PlayerResponse

    @Named("mapInjuryStatus")
    fun mapInjuryStatus(source: String) =
        if (source != PlayerStatus.QUESTIONABLE.status && source != PlayerStatus.DOUBTFUL.status) {
            PlayerStatus.OUT.status
        } else {
            source
        }

    fun mapTeam(source: Team) = source.teamName
}
