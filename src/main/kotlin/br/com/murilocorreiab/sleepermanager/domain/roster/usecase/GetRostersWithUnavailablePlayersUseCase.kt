package br.com.murilocorreiab.sleepermanager.domain.roster.usecase

import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerStatus
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster
import br.com.murilocorreiab.sleepermanager.domain.roster.gateway.RosterGateway
import javax.inject.Singleton

@Singleton
class GetRostersWithUnavailablePlayersUseCase(private val rosterGateway: RosterGateway) :
    GetRostersWithUnavailablePlayers {
    override suspend fun get(username: String): List<Roster> =
        rosterGateway.findUserRostersInLeagues(username).mapNotNull {
            val unavailableStarters =
                it.players.filter { player -> player.starter && player.injuryStatus != PlayerStatus.ACTIVE.status }
            if (unavailableStarters.isNotEmpty()) it.copy(players = unavailableStarters) else null
        }
}
