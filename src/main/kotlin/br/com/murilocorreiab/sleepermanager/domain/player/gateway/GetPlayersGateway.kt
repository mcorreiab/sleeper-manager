package br.com.murilocorreiab.sleepermanager.domain.player.gateway

import br.com.murilocorreiab.sleepermanager.entities.player.Player

interface GetPlayersGateway {

    fun getAllPlayers(): List<Player>
}
