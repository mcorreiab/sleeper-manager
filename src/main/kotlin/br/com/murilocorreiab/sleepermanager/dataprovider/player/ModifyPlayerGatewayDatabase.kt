package br.com.murilocorreiab.sleepermanager.dataprovider.player

import br.com.murilocorreiab.sleepermanager.dataprovider.player.db.PlayerRepository
import br.com.murilocorreiab.sleepermanager.dataprovider.player.db.entity.PlayerDbMapper
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import br.com.murilocorreiab.sleepermanager.domain.player.gateway.ModifyPlayerGateway
import jakarta.inject.Singleton
import org.mapstruct.factory.Mappers

@Singleton
class ModifyPlayerGatewayDatabase(private val playerRepository: PlayerRepository) : ModifyPlayerGateway {
    private val playerDbMapper = Mappers.getMapper(PlayerDbMapper::class.java)

    override fun updatePlayers(players: List<Player>): List<Player> {
        val playersToSave = playerDbMapper.fromDomain(players.toList())
        playerRepository.deleteAll()
        return playerRepository.saveAll(playersToSave).map { playerDbMapper.toDomain(it) }
    }
}
