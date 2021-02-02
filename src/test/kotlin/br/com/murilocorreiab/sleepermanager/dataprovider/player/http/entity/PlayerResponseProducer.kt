package br.com.murilocorreiab.sleepermanager.dataprovider.player.http.entity

import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.PlayerResponse
import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerStatus

data class PlayerResponseProducer(
    val playerId: String = "playerId",
    val fullName: String? = "fullName",
    val firstName: String = "firstName",
    val lastName: String = "lastName",
    val injuryStatus: String? = PlayerStatus.IR.status,
    val position: String = "position",
    val team: String = "team"
) {
    fun build() =
        PlayerResponse(
            playerId = playerId,
            fullName = fullName,
            firstName = firstName,
            lastName = lastName,
            injuryStatus = injuryStatus,
            position = position,
            team = team
        )
}
