package br.com.murilocorreiab.sleepermanager.dataprovider.player.http.entity

import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.PlayerResponse
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerStatus
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Team

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
        team = team,
    )

    fun PlayerResponse.toDomain(
        injuryStatus: PlayerStatus,
        team: Team,
        starter: Boolean,
        name: String,
        position: String,
    ) =
        Player(
            id = playerId,
            name = name,
            injuryStatus = injuryStatus.status,
            position = position,
            team = team,
            starter = starter,
        )
}
