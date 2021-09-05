package br.com.murilocorreiab.sleepermanager.dataprovider.player.http.entity

import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.PlayerResponse
import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerStatus

object PlayerResponseProducer {
    fun build(
        playerId: String = "playerId",
        fullName: String? = "fullName",
        firstName: String = "firstName",
        lastName: String = "lastName",
        injuryStatus: String? = PlayerStatus.IR.status,
        position: String? = "position",
        team: String? = "GB"
    ) = PlayerResponse(
        playerId = playerId,
        fullName = fullName,
        firstName = firstName,
        lastName = lastName,
        injuryStatus = injuryStatus,
        position = position,
        team = team
    )
}
