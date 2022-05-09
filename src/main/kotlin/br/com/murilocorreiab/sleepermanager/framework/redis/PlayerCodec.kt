package br.com.murilocorreiab.sleepermanager.framework.redis

import br.com.murilocorreiab.sleepermanager.entities.player.Player
import com.fasterxml.jackson.databind.ObjectMapper
import io.lettuce.core.codec.RedisCodec
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

class PlayerCodec(private val objectMapper: ObjectMapper) : RedisCodec<String, Player> {

    override fun decodeKey(bytes: ByteBuffer?): String? =
        bytes?.let { StandardCharsets.UTF_8.decode(it).toString() }

    override fun decodeValue(bytes: ByteBuffer?): Player? =
        bytes?.let { objectMapper.readValue(StandardCharsets.UTF_8.decode(it).toString(), Player::class.java) }

    override fun encodeKey(key: String?): ByteBuffer? = key?.let { ByteBuffer.wrap(it.encodeToByteArray()) }

    override fun encodeValue(value: Player?): ByteBuffer = ByteBuffer.wrap(objectMapper.writeValueAsBytes(value))
}
