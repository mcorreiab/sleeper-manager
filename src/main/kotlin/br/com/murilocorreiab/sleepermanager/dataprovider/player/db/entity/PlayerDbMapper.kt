package br.com.murilocorreiab.sleepermanager.dataprovider.player.db.entity

import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import org.mapstruct.Mapper

@Mapper
interface PlayerDbMapper {

    fun fromDomain(player: Player): PlayerDb

    fun fromDomain(players: List<Player>): List<PlayerDb>

    fun toDomain(player: PlayerDb): Player
}
