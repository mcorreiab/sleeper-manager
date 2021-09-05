package br.com.murilocorreiab.sleepermanager.dataprovider.player

import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.GetPlayers
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import br.com.murilocorreiab.sleepermanager.domain.player.gateway.GetPlayersGateway
import jakarta.inject.Singleton

@Singleton
class GetPlayersGatewayDataProvider(private val getPlayers: GetPlayers) : GetPlayersGateway {
    override fun getPlayersInformation(players: List<String>): List<Player> =
        getAllPlayers().filter { players.any { player -> it.name.contains(player, true) } }

    override fun getAllPlayers(): List<Player> = getPlayers.getAllPlayers()
}
