package br.com.murilocorreiab.sleepermanager.dataprovider.roster

import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueMapper
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueResponse
import br.com.murilocorreiab.sleepermanager.dataprovider.player.db.PlayerRepository
import br.com.murilocorreiab.sleepermanager.dataprovider.player.db.entity.PlayerDbMapper
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.RosterResponse
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.RosterResponseMapper
import br.com.murilocorreiab.sleepermanager.domain.league.entity.League
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster
import br.com.murilocorreiab.sleepermanager.domain.roster.gateway.RosterGateway
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.coroutineScope
import org.mapstruct.factory.Mappers
import javax.inject.Singleton

@Singleton
class RosterGatewayHttpClient(
    private val getRostersInUserLeagues: GetRostersInUserLeagues,
    private val playerRepository: PlayerRepository
) : RosterGateway {

    private val rosterResponseMapper = Mappers.getMapper(RosterResponseMapper::class.java)
    private val leagueResponseMapper = Mappers.getMapper(LeagueMapper::class.java)
    private val playerDbMapper = Mappers.getMapper(PlayerDbMapper::class.java)

    override suspend fun findUserRostersInLeagues(username: String): List<Roster> = coroutineScope {
        val rostersByLeague = getRostersInUserLeagues.getUserRosters(username)

        rostersByLeague.flatMap { (league, rosters) ->
            rosters.mapNotNull { roster -> mapRosters(roster, league) }
        }
    }

    suspend fun findUserRosterByUserIdInLeagues(userId: String): List<Roster> = coroutineScope {
        val rostersByLeague = getRostersInUserLeagues.getUserRostersById(userId)

        rostersByLeague.flatMap { (league, rosters) ->
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
        roster.players.mapNotNull {
            playerRepository.findById(it).orElse(null)
        }.map {
            val player = playerDbMapper.toDomain(it)
            player.starter = roster.starters.contains(player.id)
            player
        }

    @FlowPreview
    override suspend fun findAllRosteredPlayersInUserLeagues(username: String): List<Pair<League, List<Player>>> {

        val rostersByLeague = getRostersInUserLeagues.getAllRosters(username)

        return rostersByLeague.map { (league, rosters) ->
            val rosterPlayers = getPlayersFromRosters(rosters)
            val domainLeague = leagueResponseMapper.convertToDomain(league)
            Pair(domainLeague, rosterPlayers)
        }
    }

    @FlowPreview
    private fun getPlayersFromRosters(
        rosters: List<RosterResponse>
    ): List<Player> = rosters.flatMap { it.players }
        .distinct()
        .mapNotNull {
            playerRepository.findById(it).orElse(null)
        }.map {
            playerDbMapper.toDomain(it)
        }
}
