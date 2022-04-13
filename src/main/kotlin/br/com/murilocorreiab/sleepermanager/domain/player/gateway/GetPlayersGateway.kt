package br.com.murilocorreiab.sleepermanager.domain.player.gateway

import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player

interface GetPlayersGateway {

    fun getAllPlayers(): List<Player>
}
