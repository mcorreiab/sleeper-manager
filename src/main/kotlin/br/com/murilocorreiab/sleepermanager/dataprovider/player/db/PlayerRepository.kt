package br.com.murilocorreiab.sleepermanager.dataprovider.player.db

import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import io.lettuce.core.SetArgs
import io.lettuce.core.api.StatefulRedisConnection
import jakarta.inject.Singleton
import java.time.Duration

@Singleton
class PlayerRepository(private val connection: StatefulRedisConnection<String, Player>) {

    fun getAll() = connection.sync().keys("*").map { connection.async().get(it) }.mapNotNull { it.get() }

    fun getById(id: String): Player? = connection.sync().get(id)

    fun create(players: List<Player>) {
        val asyncCommands = connection.async()
        players.forEach { asyncCommands.set(it.id, it, SetArgs.Builder.ex(Duration.ofHours(1))) }
    }
}
