package br.com.murilocorreiab.sleepermanager.dataprovider.roster

import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.LeagueClient
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.UserClient
import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.LeagueResponse
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.PlayerClient
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.RosterClient
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.PlayerResponse
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.PlayerResponseMapper
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.RosterResponse
import br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity.RosterResponseMapper
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Player
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster
import br.com.murilocorreiab.sleepermanager.domain.roster.gateway.RosterGateway
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
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
    private val rosterResponseMapper = Mappers.getMapper(RosterResponseMapper::class.java)

    @FlowPreview
    override suspend fun findUserRostersInLeagues(username: String): Flow<Roster> {
        val userResponse = userClient.getByUsername(username).awaitFirst()
        val allPlayers = playerClient.getAllPlayers()
        return leagueClient.getByUserId(userResponse.userId).asFlow().flatMapConcat {
            mapLeagueRosters(it, userResponse.userId, allPlayers)
        }
    }

    private fun mapLeagueRosters(
        leagueResponse: LeagueResponse,
        userId: String,
        allPlayers: Map<String, PlayerResponse>
    ): Flow<Roster> = rosterClient.getRostersOfALeague(leagueResponse.leagueId).filter { it.ownerId == userId }
        .asFlow().mapNotNull { mapRosters(it, allPlayers, leagueResponse) }

    private fun mapRosters(
        roster: RosterResponse,
        allPlayers: Map<String, PlayerResponse>,
        leagueResponse: LeagueResponse
    ) = mapPlayers(roster, allPlayers).let {
        if (it.isNotEmpty()) rosterResponseMapper.toDomain(roster, leagueResponse, it) else null
    }

    private fun mapPlayers(roster: RosterResponse, allPlayers: Map<String, PlayerResponse>): List<Player> =
        roster.players.mapNotNull {
            allPlayers[it]?.let { player -> playerResponseMapper.toDomain(player, roster.starters) }
        }
}
