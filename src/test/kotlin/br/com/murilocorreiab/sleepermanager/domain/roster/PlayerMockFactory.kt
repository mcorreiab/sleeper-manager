package br.com.murilocorreiab.sleepermanager.domain.roster

import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerFactory
import br.com.murilocorreiab.sleepermanager.entities.player.PlayerStatus
import br.com.murilocorreiab.sleepermanager.usecase.PlayerGateway
import io.mockk.every

class PlayerMockFactory(id: String, playerStatus: PlayerStatus, playerGateway: PlayerGateway) {
    private val player = PlayerFactory.build(id = id, injuryStatus = playerStatus)

    init {
        every { playerGateway.getById(id) } returns player
    }

    fun get() = player
}
