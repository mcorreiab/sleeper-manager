package br.com.murilocorreiab.sleepermanager.dataprovider.player.db

import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player

interface PlayerRepository {
    fun getAll(): List<Player>
    fun getById(id: String): Player?
    fun create(players: List<Player>)
}
