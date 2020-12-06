package br.com.murilocorreiab.sleepermanager.dataprovider.player

import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.PlayerClient
import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.PlayerResponse
import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.PlayerResponseMapper
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import br.com.murilocorreiab.sleepermanager.domain.player.gateway.GetPlayersGateway
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.mapstruct.factory.Mappers
import javax.inject.Singleton

@Singleton
class GetPlayersGatewayHttpClient(private val playerClient: PlayerClient) : GetPlayersGateway {
    private val playerMapper = Mappers.getMapper(PlayerResponseMapper::class.java)
    override suspend fun getPlayersInformation(players: List<String>): Flow<Player> {
        val allPlayers = playerClient.getAllPlayers()
        val foundPlayers = allPlayers.values.filter {
            filterPlayersByName(players, it)
        }.map { playerMapper.toDomain(it) }
        return flowOf(*foundPlayers.toTypedArray())
    }

    private fun filterPlayersByName(
        players: List<String>,
        playerResponse: PlayerResponse
    ) = players.any {
        val name = playerResponse.fullName ?: "${playerResponse.firstName} ${playerResponse.lastName}"
        name.contains(it, true)
    }

    override suspend fun getAllPlayers(): Flow<Player> {
        TODO("Not yet implemented")
    }
}
