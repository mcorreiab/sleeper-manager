package br.com.murilocorreiab.sleepermanager.dataprovider.player.db.entity

data class PlayerDbProducer(
    val id: String = "id",
    val name: String = "name",
    val injuryStatus: String = "injuryStatus"
) {
    fun build() = PlayerDb(id, name, injuryStatus)
}
