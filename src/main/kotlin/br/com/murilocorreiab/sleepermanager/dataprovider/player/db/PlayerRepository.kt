package br.com.murilocorreiab.sleepermanager.dataprovider.player.db

import br.com.murilocorreiab.sleepermanager.entities.player.Player

interface PlayerRepository {
    fun getAll(): List<Player>
    fun getById(id: String): Player?
    fun create(players: List<Player>)
}
