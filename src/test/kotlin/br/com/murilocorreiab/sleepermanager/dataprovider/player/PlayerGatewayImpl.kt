package br.com.murilocorreiab.sleepermanager.dataprovider.player

import br.com.murilocorreiab.sleepermanager.dataprovider.player.db.PlayerRepository
import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.PlayerClient
import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.PlayerResponseMapper
import br.com.murilocorreiab.sleepermanager.domain.player.gateway.PlayerGateway
import jakarta.inject.Singleton
import org.mapstruct.factory.Mappers

@Singleton
class PlayerGatewayImpl(
    private val playerClient: PlayerClient,
    private val playerRepository: PlayerRepository,
) : PlayerGateway {
    private val playerResponseMapper = Mappers.getMapper(PlayerResponseMapper::class.java)

    override fun getAllPlayers() = playerRepository.getAll().ifEmpty {
        playerClient.getAllPlayers().values.map { playerResponseMapper.toDomain(it) }
            .also { playerRepository.create(it) }
    }
}
