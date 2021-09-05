package br.com.murilocorreiab.sleepermanager.dataprovider.config

import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import com.fasterxml.jackson.databind.ObjectMapper
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.api.StatefulRedisConnection
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value

@Factory
class RedisConfig(
    private val redisClient: RedisClient,
    private val objectMapper: ObjectMapper,
    @Value("\${redis.uri}") private val redisUri: String,
) {

    @Bean
    fun playerConnection(): StatefulRedisConnection<String, Player> {
        return redisClient.connect(PlayerCodec(objectMapper), RedisURI.create(redisUri))
    }
}
