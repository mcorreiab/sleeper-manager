package br.com.murilocorreiab.sleepermanager.dataprovider.player.http.entity

import br.com.murilocorreiab.sleepermanager.adapters.player.PlayerExternalResponse
import br.com.murilocorreiab.sleepermanager.entities.player.Player
import br.com.murilocorreiab.sleepermanager.entities.player.PlayerStatus
import br.com.murilocorreiab.sleepermanager.entities.player.Team

object PlayerResponseProducer {
    fun build(
        playerId: String = "playerId",
        fullName: String? = "fullName",
        firstName: String = "firstName",
        lastName: String = "lastName",
        injuryStatus: String? = PlayerStatus.IR.status,
        position: String? = "position",
        team: String? = "GB"
    ) = PlayerExternalResponse(
        playerId = playerId,
        fullName = fullName,
        firstName = firstName,
        lastName = lastName,
        injuryStatus = injuryStatus,
        position = position,
        team = team,
    )

    fun PlayerExternalResponse.toDomain(
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
