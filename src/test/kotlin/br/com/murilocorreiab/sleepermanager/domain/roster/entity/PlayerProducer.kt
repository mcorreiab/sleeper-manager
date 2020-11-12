package br.com.murilocorreiab.sleepermanager.domain.roster.entity

data class PlayerProducer(
    val id: String = "playerId",
    val name: String = "playerName",
    val status: String = PlayerStatus.ACTIVE.status,
    val isStarter: Boolean = true
) {
    fun build() = Player(id, name, status, isStarter = isStarter)
}
