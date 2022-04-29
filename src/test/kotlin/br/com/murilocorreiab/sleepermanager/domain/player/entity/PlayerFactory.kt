package br.com.murilocorreiab.sleepermanager.domain.player.entity

import br.com.murilocorreiab.sleepermanager.entities.player.Player
import br.com.murilocorreiab.sleepermanager.entities.player.PlayerStatus
import br.com.murilocorreiab.sleepermanager.entities.player.Team

object PlayerFactory {
    fun build(
        id: String = "playerId",
        name: String = "playerName",
        injuryStatus: PlayerStatus = PlayerStatus.OUT,
        starter: Boolean = true,
        position: String = "position",
        team: Team = Team.GB,
    ) = Player(id, name, injuryStatus.status, starter, position, team)
}
