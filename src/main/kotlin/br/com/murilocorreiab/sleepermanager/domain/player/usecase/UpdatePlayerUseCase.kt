package br.com.murilocorreiab.sleepermanager.domain.player.usecase

import br.com.murilocorreiab.sleepermanager.domain.player.gateway.GetPlayersGateway
import br.com.murilocorreiab.sleepermanager.domain.player.gateway.ModifyPlayerGateway
import javax.inject.Singleton

@Singleton
class UpdatePlayerUseCase(
    private val getPlayersGateway: GetPlayersGateway,
    private val modifyPlayerGateway: ModifyPlayerGateway
) : UpdatePlayer {
    override suspend fun updatePlayers() = getPlayersGateway.getAllPlayers().let {
        modifyPlayerGateway.updatePlayers(it)
    }
}
