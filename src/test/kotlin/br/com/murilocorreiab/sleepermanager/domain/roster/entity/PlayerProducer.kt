package br.com.murilocorreiab.sleepermanager.domain.roster.entity

data class PlayerProducer(
    val id: String = "playerId",
    val name: String = "playerName",
    val status: PlayerStatus = PlayerStatus.ACTIVE,
    val isStarter: Boolean = true
) {
    fun build() = Player(id, name, status, isStarter = isStarter)
}
