package br.com.murilocorreiab.sleepermanager.dataprovider.player

import br.com.murilocorreiab.sleepermanager.dataprovider.player.db.PlayerRepository
import br.com.murilocorreiab.sleepermanager.dataprovider.player.db.entity.PlayerDbMapper
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import br.com.murilocorreiab.sleepermanager.domain.player.gateway.ModifyPlayerGateway
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import org.mapstruct.factory.Mappers
import javax.inject.Singleton

@Singleton
class ModifyPlayerGatewayDatabase(private val playerRepository: PlayerRepository) : ModifyPlayerGateway {
    private val playerDbMapper = Mappers.getMapper(PlayerDbMapper::class.java)

    override suspend fun updatePlayers(players: Flow<Player>): Flow<Player> {
        val playersToSave = playerDbMapper.fromDomain(players.toList())
        playerRepository.deleteAll()
        return playerRepository.saveAll(playersToSave).asFlow().map { playerDbMapper.toDomain(it) }
    }
}
