package br.com.murilocorreiab.sleepermanager.dataprovider.config

import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import com.fasterxml.jackson.databind.ObjectMapper
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.api.StatefulRedisConnection
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Factory
class RedisConfig(
    private val redisClient: RedisClient,
    private val objectMapper: ObjectMapper,
    @Value("\${redis.uri}") private val redisUri: String,
) {

    private val logger: Logger = LoggerFactory.getLogger(RedisConfig::class.java)

    @Bean
    fun playerConnection(): StatefulRedisConnection<String, Player> {
        logger.error("URI to use $redisUri")
        return redisClient.connect(PlayerCodec(objectMapper), RedisURI.create(redisUri))
    }
}
