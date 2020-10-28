package br.com.murilocorreiab.sleepermanager.dataprovider.roster

import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.LeagueClient
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.UserClient
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueResponse
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.PlayerClient
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.RosterClient
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.PlayerResponse
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.PlayerResponseMapper
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.RosterResponse
import br.com.murilocorreiab.sleepermanager.domain.league.entity.League
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Player
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster
import br.com.murilocorreiab.sleepermanager.domain.roster.gateway.RosterGateway
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.mapstruct.factory.Mappers
import javax.inject.Singleton

@Singleton
class RosterGatewayHttpClient(
    private val userClient: UserClient,
    private val leagueClient: LeagueClient,
    private val rosterClient: RosterClient,
    private val playerClient: PlayerClient,
) : RosterGateway {

    private val playerResponseMapper = Mappers.getMapper(PlayerResponseMapper::class.java)

    @FlowPreview
    override suspend fun findUserRostersInLeagues(username: String): Flow<Roster> {
        val userResponse = userClient.getByUsername(username)
        val allPlayers = playerClient.getAllPlayers().toList()
        return leagueClient.getByUserId(userResponse.userId).flatMapConcat {
            mapLeagueRosters(it, userResponse.userId, allPlayers)
        }
    }

    private fun mapLeagueRosters(
        leagueResponse: LeagueResponse,
        userId: String,
        allPlayers: List<PlayerResponse>
    ): Flow<Roster> =
        rosterClient.getRostersOfALeague(leagueResponse.leagueId).filter { roster -> roster.ownerId == userId }
            .map { roster ->
                val players = mapPlayers(roster, allPlayers)
                mapRoster(roster, userId, players, leagueResponse)
            }

    private fun mapRoster(
        roster: RosterResponse,
        userId: String,
        players: List<Player>,
        leagueResponse: LeagueResponse
    ): Roster = Roster(
        id = roster.rosterId,
        ownerId = userId,
        players = players,
        league = League(
            name = leagueResponse.name,
            id = leagueResponse.leagueId,
            size = leagueResponse.totalRosters
        )
    )

    private fun mapPlayers(roster: RosterResponse, allPlayers: List<PlayerResponse>): List<Player> =
        allPlayers.filter { roster.players.contains(it.playerId) }
            .map { playerResponseMapper.toDomain(it, roster.starters) }
}
