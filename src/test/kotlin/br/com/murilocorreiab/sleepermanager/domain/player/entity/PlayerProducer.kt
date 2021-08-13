package br.com.murilocorreiab.sleepermanager.domain.player.entity

data class PlayerProducer(
    val id: String = "playerId",
    val name: String = "playerName",
    val injuryStatus: PlayerStatus = PlayerStatus.OUT,
    val starter: Boolean = true,
    val position: String = "position",
    val team: String = "team"
) {
    fun build() = Player(id, name, injuryStatus.status, starter, position, team)
}
