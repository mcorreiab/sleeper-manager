package br.com.murilocorreiab.sleepermanager.dataprovider.player.db.entity

import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper
interface PlayerDbMapper {

    fun fromDomain(player: Player): PlayerDb

    fun fromDomain(players: List<Player>): List<PlayerDb>

    @Mapping(target = "starter", constant = "false")
    fun toDomain(player: PlayerDb): Player
}
