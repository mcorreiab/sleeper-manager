package br.com.murilocorreiab.sleepermanager.dataprovider.player

import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.GetPlayers
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import br.com.murilocorreiab.sleepermanager.domain.player.gateway.GetPlayersGateway
import jakarta.inject.Singleton

@Singleton
class GetPlayersGatewayDataProvider(private val getPlayers: GetPlayers) : GetPlayersGateway {

    override fun getAllPlayers(): List<Player> = getPlayers.getAllPlayers()
}
