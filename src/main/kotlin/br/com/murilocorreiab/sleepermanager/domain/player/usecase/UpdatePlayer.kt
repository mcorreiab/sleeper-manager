package br.com.murilocorreiab.sleepermanager.domain.player.usecase

import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player

interface UpdatePlayer {
    fun updatePlayers(): List<Player>
}
