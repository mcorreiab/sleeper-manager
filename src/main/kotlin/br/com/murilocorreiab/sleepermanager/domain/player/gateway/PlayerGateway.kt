package br.com.murilocorreiab.sleepermanager.domain.player.gateway

import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player

interface PlayerGateway {
    fun getAllPlayers(): List<Player>
    fun getById(playerId: String): Player?
}
