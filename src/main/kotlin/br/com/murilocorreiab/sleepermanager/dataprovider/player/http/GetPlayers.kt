package br.com.murilocorreiab.sleepermanager.dataprovider.player.http

import br.com.murilocorreiab.sleepermanager.dataprovider.player.db.PlayerRepositoryRedis
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import jakarta.inject.Singleton
import org.mapstruct.factory.Mappers

@Singleton
class GetPlayers(
    private val playerClient: PlayerClient,
    private val playerRepository: PlayerRepositoryRedis
) {
    private val playerMapper = Mappers.getMapper(PlayerResponseMapper::class.java)

    fun getAllPlayers(): List<Player> =
        playerRepository.getAll().ifEmpty {
            playerClient.getAllPlayers().values.map {
                playerMapper.toDomain(it)
            }.also { playerRepository.create(it) }
        }

    fun getPlayerById(id: String): Player? =
        playerRepository.getById(id) ?: getAllPlayers().firstOrNull { it.id == id }
}
