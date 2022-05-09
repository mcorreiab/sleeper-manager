package br.com.murilocorreiab.sleepermanager.adapters.player

import br.com.murilocorreiab.sleepermanager.adapters.BadRequestException
import br.com.murilocorreiab.sleepermanager.adapters.NotFoundException
import br.com.murilocorreiab.sleepermanager.usecase.GetPlayersOutOfRoster
import kotlinx.coroutines.runBlocking

class PlayerInWaiverController(private val getPlayersInWaiver: GetPlayersOutOfRoster) {

    fun getPlayersInWaiverByLeague(
        userId: String,
        players: String?
    ): List<PlayersWaiverResponse> = runBlocking {
        players?.let {
            val namesToSearch = getNamesToSearch(it)
            if (namesToSearch.isNotEmpty()) {
                getPlayers(userId, namesToSearch)
            } else {
                throw BadRequestException()
            }
        } ?: throw BadRequestException()
    }

    private fun getNamesToSearch(playersQuery: String): List<String> =
        playersQuery.split(",").filter { it.isNotBlank() }.map { it.trim() }

    private fun getPlayers(
        userId: String,
        namesToSearch: List<String>
    ): List<PlayersWaiverResponse> {
        val playersInWaiver = doGetPlayersInWaiver(userId, namesToSearch)
        return playersInWaiver.ifEmpty {
            throw NotFoundException()
        }
    }

    private fun doGetPlayersInWaiver(
        userId: String,
        namesToSearch: List<String>
    ) = getPlayersInWaiver.get(userId, namesToSearch).map { (player, leagues) ->
        PlayersWaiverResponse(player, leagues)
    }
}
