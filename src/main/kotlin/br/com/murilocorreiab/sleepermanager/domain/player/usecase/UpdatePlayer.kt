package br.com.murilocorreiab.sleepermanager.domain.player.usecase

import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player

interface UpdatePlayer {
    suspend fun updatePlayers(): List<Player>
}
