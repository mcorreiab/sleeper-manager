package br.com.murilocorreiab.sleepermanager.dataprovider.roster

import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueMapper
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueResponse
import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.GetPlayers
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.RosterResponse
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.RosterResponseMapper
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster
import br.com.murilocorreiab.sleepermanager.domain.roster.gateway.RosterGateway
import jakarta.inject.Singleton
import org.mapstruct.factory.Mappers

@Singleton
class RosterGatewayHttpClient(
    private val getRostersInUserLeagues: GetRostersInUserLeagues,
    private val getPlayers: GetPlayers,
) : RosterGateway {

    private val rosterResponseMapper = Mappers.getMapper(RosterResponseMapper::class.java)
    private val leagueResponseMapper = Mappers.getMapper(LeagueMapper::class.java)

    override fun findUserRostersByUsernameInLeagues(username: String): List<Roster> {
        val rostersByLeague = getRostersInUserLeagues.getUserRosters(username)

        return rostersByLeague.flatMap { (league, rosters) ->
            rosters.mapNotNull { roster -> mapRosters(roster, league) }
        }
    }

    override fun findUserRostersByUserIdInLeagues(userId: String): List<Roster> {
        val rostersByLeague = getRostersInUserLeagues.getUserRostersById(userId)

        return rostersByLeague.flatMap { (league, rosters) ->
            rosters.mapNotNull { roster -> mapRosters(roster, league) }
        }
    }

    private fun mapRosters(
        roster: RosterResponse,
        leagueResponse: LeagueResponse
    ) = mapPlayers(roster).let {
        if (it.isNotEmpty()) rosterResponseMapper.toDomain(roster, leagueResponse, it) else null
    }

    private fun mapPlayers(roster: RosterResponse): List<Player> =
        roster.players?.mapNotNull { getPlayers.getPlayerById(it) }?.map {
            it.starter = roster.starters.contains(it.id)
            it
        } ?: emptyList()
}
