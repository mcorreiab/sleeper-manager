package br.com.murilocorreiab.sleepermanager.adapters.player

interface PlayerClient {
    fun getAllPlayers(): Map<String, PlayerExternalResponse>
}
