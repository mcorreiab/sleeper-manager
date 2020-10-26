package br.com.murilocorreiab.sleepermanager.domain.roster.entity

data class PlayerProducer(val id: String = "playerId", val name: String = "playerName") {
    fun build() = Player(id, name)
}
