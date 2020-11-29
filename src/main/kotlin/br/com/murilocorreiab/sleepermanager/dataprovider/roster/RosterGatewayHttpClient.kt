package br.com.murilocorreiab.sleepermanager.dataprovider.roster

import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueMapper
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueResponse
import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.PlayerClient
import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.PlayerResponse
import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.PlayerResponseMapper
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.RosterResponse
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.RosterResponseMapper
import br.com.murilocorreiab.sleepermanager.domain.league.entity.League
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster
import br.com.murilocorreiab.sleepermanager.domain.roster.gateway.RosterGateway
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import org.mapstruct.factory.Mappers
import javax.inject.Singleton

@Singleton
class RosterGatewayHttpClient(
    private val getRostersInUserLeagues: GetRostersInUserLeagues,
    private val playerClient: PlayerClient,
) : RosterGateway {

    private val playerResponseMapper = Mappers.getMapper(PlayerResponseMapper::class.java)
    private val rosterResponseMapper = Mappers.getMapper(RosterResponseMapper::class.java)
    private val leagueResponseMapper = Mappers.getMapper(LeagueMapper::class.java)

    @FlowPreview
    override suspend fun findUserRostersInLeagues(username: String): Flow<Roster> = coroutineScope {
        val rostersByLeague = async { getRostersInUserLeagues.getUserRosters(username) }

        val allPlayers = async { playerClient.getAllPlayers() }

        rostersByLeague.await().flatMapConcat { (league, rosters) ->
            rosters.mapNotNull { roster -> mapRosters(roster, allPlayers.await(), league) }
        }
    }

    private fun mapRosters(
        roster: RosterResponse,
        allPlayers: Map<String, PlayerResponse>,
        leagueResponse: LeagueResponse
    ) = mapPlayers(roster, allPlayers).let {
        if (it.isNotEmpty()) rosterResponseMapper.toDomain(roster, leagueResponse, it) else null
    }

    private fun mapPlayers(roster: RosterResponse, allPlayers: Map<String, PlayerResponse>): List<Player> =
        roster.players.mapNotNull {
            allPlayers[it]?.let { player ->
                val domainPlayer = playerResponseMapper.toDomain(player)
                domainPlayer.starter = roster.starters.contains(player.playerId)
                domainPlayer
            }
        }

    @FlowPreview
    override suspend fun findAllRosteredPlayersInUserLeagues(username: String): Flow<Pair<League, Flow<Player>>> =
        coroutineScope {
            val rostersByLeague = async { getRostersInUserLeagues.getAllRosters(username) }
            val players = async { playerClient.getAllPlayers() }

            rostersByLeague.await().map { (league, rosters) ->
                val rosterPlayers = getPlayersFromRosters(rosters, players.await())
                val domainLeague = leagueResponseMapper.convertToDomain(league)
                Pair(domainLeague, rosterPlayers)
            }
        }

    @FlowPreview
    private fun getPlayersFromRosters(
        rosters: Flow<RosterResponse>,
        players: Map<String, PlayerResponse>
    ): Flow<Player> = rosters.flatMapConcat { flowOf(*it.players.toTypedArray()) }
        .distinctUntilChanged()
        .mapNotNull { players[it]?.let { player -> playerResponseMapper.toDomain(player) } }
}
