package br.com.murilocorreiab.sleepermanager.adapters

import br.com.murilocorreiab.sleepermanager.usecase.GetUnavailableStarterPlayers

class UnavailableStarterPlayersController(private val getUnavailableStarterPlayers: GetUnavailableStarterPlayers) {
    fun getByUserId(userId: String) = getUnavailableStarterPlayers.get(userId).takeIf { it.isNotEmpty() }
        ?: throw NotFoundException() // TODO create controller advice
}
