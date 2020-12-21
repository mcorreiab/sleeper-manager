package br.com.murilocorreiab.sleepermanager.domain.player.gateway

import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player

interface GetPlayersGateway {

    suspend fun getPlayersInformation(players: List<String>): List<Player>
    suspend fun getAllPlayers(): List<Player>
}
