package br.com.murilocorreiab.sleepermanager.dataprovider.player.db.entity

data class PlayerDbProducer(
    val id: String = "playerId",
    val starter: Boolean = false,
    val name: String = "name",
    val injuryStatus: String = "injuryStatus"
) {
    fun build() = PlayerDb(id = id, starter = starter, name = name, injuryStatus = injuryStatus)
}
