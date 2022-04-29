package br.com.murilocorreiab.sleepermanager.dataprovider.player

import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.GetPlayers
import br.com.murilocorreiab.sleepermanager.domain.player.gateway.GetPlayersGateway
import br.com.murilocorreiab.sleepermanager.entities.player.Player
import jakarta.inject.Singleton

@Singleton
class GetPlayersGatewayDataProvider(private val getPlayers: GetPlayers) : GetPlayersGateway {

    override fun getAllPlayers(): List<Player> = getPlayers.getAllPlayers()
}
