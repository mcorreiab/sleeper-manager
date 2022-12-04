package br.com.murilocorreiab.sleepermanager.adapters.player

import br.com.murilocorreiab.sleepermanager.usecase.PlayerGateway

class PlayerGatewayImpl(
    private val playerClient: PlayerClient,
    private val playerRepository: PlayerRepository,
    private val playerResponseMapper: PlayerExternalResponseMapper,
) : PlayerGateway {

    override fun getAllPlayers() = playerRepository.getAll().ifEmpty {
        playerClient.getAllPlayers().values.map { playerResponseMapper.toDomain(it) }
            .also { playerRepository.create(it) }
    }

    override fun getById(playerId: String) =
        playerRepository.getById(playerId) ?: getAllPlayers().find { it.id == playerId }
}
