package br.com.murilocorreiab.sleepermanager.dataprovider.player.db

import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import io.lettuce.core.SetArgs
import io.lettuce.core.api.StatefulRedisConnection
import jakarta.inject.Singleton
import java.time.Duration

@Singleton
class PlayerRepositoryImpl(private val connection: StatefulRedisConnection<String, Player>) : PlayerRepository {

    override fun getAll() = connection.sync().keys("*").map { connection.async().get(it) }.mapNotNull { it.get() }

    override fun getById(id: String): Player? = connection.sync().get(id)

    override fun create(players: List<Player>) {
        val asyncCommands = connection.async()
        players.forEach { asyncCommands.set(it.id, it, SetArgs.Builder.ex(Duration.ofHours(1))) }
    }
}
