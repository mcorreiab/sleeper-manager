package br.com.murilocorreiab.sleepermanager.adapters.player

import br.com.murilocorreiab.sleepermanager.usecase.PlayerGateway
import org.mapstruct.factory.Mappers

class PlayerGatewayImpl(
    private val playerClient: PlayerClient,
    private val playerRepository: PlayerRepository,
) : PlayerGateway {
    private val playerResponseMapper = Mappers.getMapper(PlayerExternalResponseMapper::class.java)

    override fun getAllPlayers() = playerRepository.getAll().ifEmpty {
        playerClient.getAllPlayers().values.map { playerResponseMapper.toDomain(it) }
            .also { playerRepository.create(it) }
    }

    override fun getById(playerId: String) =
        playerRepository.getById(playerId) ?: getAllPlayers().find { it.id == playerId }
}
