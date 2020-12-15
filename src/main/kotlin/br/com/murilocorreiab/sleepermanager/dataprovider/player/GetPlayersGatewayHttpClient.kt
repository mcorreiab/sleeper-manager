package br.com.murilocorreiab.sleepermanager.dataprovider.player

import br.com.murilocorreiab.sleepermanager.dataprovider.player.db.PlayerRepository
import br.com.murilocorreiab.sleepermanager.dataprovider.player.db.entity.PlayerDbMapper
import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.PlayerClient
import br.com.murilocorreiab.sleepermanager.dataprovider.player.http.PlayerResponseMapper
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import br.com.murilocorreiab.sleepermanager.domain.player.gateway.GetPlayersGateway
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.mapstruct.factory.Mappers
import javax.inject.Singleton

@Singleton
class GetPlayersGatewayHttpClient(
    private val playerClient: PlayerClient,
    private val playerRepository: PlayerRepository
) : GetPlayersGateway {
    private val playerMapper = Mappers.getMapper(PlayerResponseMapper::class.java)
    private val playerDbMapper = Mappers.getMapper(PlayerDbMapper::class.java)
    override suspend fun getPlayersInformation(players: List<String>): List<Player> =
        players.map {
            playerRepository.findByNameIlike("%$it%")
        }.reduce { allPlayers, foundPlayers ->
            allPlayers + foundPlayers.filter { !allPlayers.contains(it) }
        }.map {
            playerDbMapper.toDomain(it)
        }

    override suspend fun getAllPlayers(): Flow<Player> = flowOf(
        *playerClient.getAllPlayers().values.map {
            playerMapper.toDomain(it)
        }.toTypedArray()
    )
}
