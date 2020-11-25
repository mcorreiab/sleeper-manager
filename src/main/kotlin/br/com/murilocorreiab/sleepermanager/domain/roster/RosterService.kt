package br.com.murilocorreiab.sleepermanager.domain.roster

import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerStatus
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster
import br.com.murilocorreiab.sleepermanager.domain.roster.gateway.RosterGateway
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Singleton

@Singleton
class RosterService(private val rosterGateway: RosterGateway) {
    suspend fun checkForUnavailablePlayers(username: String): Flow<Roster> =
        rosterGateway.findUserRostersInLeagues(username).mapNotNull {
            val unavailableStarters =
                it.players.filter { player -> player.starter && player.injuryStatus != PlayerStatus.ACTIVE.status }
            if (unavailableStarters.isNotEmpty()) it.copy(players = unavailableStarters) else null
        }
}
