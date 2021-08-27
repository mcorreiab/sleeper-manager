package br.com.murilocorreiab.sleepermanager.domain.roster.gateway

import br.com.murilocorreiab.sleepermanager.domain.league.entity.League
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster

interface RosterGateway {
    fun findUserRostersByUsernameInLeagues(username: String): List<Roster>
    fun findUserRostersByUserIdInLeagues(userId: String): List<Roster>
    fun findAllRosteredPlayersInUserLeagues(username: String): List<Pair<League, List<Player>>>
}
