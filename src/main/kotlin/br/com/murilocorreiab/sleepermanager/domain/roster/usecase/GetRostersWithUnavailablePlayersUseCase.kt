package br.com.murilocorreiab.sleepermanager.domain.roster.usecase

import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster
import br.com.murilocorreiab.sleepermanager.domain.roster.gateway.RosterGateway
import br.com.murilocorreiab.sleepermanager.entities.player.PlayerStatus
import jakarta.inject.Singleton

@Singleton
class GetRostersWithUnavailablePlayersUseCase(private val rosterGateway: RosterGateway) :
    GetRostersWithUnavailablePlayers {
    override fun getByUsername(username: String): List<Roster> =
        rosterGateway.findUserRostersByUsernameInLeagues(username).mapNotNull {
            getRosterWithUnavailableStarter(it)
        }

    override fun getByUserId(userId: String): List<Roster> =
        rosterGateway.findUserRostersByUserIdInLeagues(userId).mapNotNull {
            getRosterWithUnavailableStarter(it)
        }

    private fun getRosterWithUnavailableStarter(it: Roster): Roster? {
        val unavailableStarters =
            it.players.filter { player -> player.starter && player.injuryStatus != PlayerStatus.ACTIVE.status }
        return if (unavailableStarters.isNotEmpty()) it.copy(players = unavailableStarters) else null
    }
}
