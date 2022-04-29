package br.com.murilocorreiab.sleepermanager.usecase

import br.com.murilocorreiab.sleepermanager.entities.player.Player

interface PlayerGateway {
    fun getAllPlayers(): List<Player>
    fun getById(playerId: String): Player?
}
