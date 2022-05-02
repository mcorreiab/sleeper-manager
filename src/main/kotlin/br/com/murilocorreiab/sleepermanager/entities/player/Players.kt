package br.com.murilocorreiab.sleepermanager.entities.player

class Players(private val players: List<Player>) {
    fun filterByName(nameFilters: List<String>): List<Player> = if (nameFilters.isEmpty()) {
        players
    } else {
        filterPlayerWithNameMatchesAnyFilter(nameFilters)
    }

    private fun filterPlayerWithNameMatchesAnyFilter(nameFilters: List<String>) = players.filter {
        it.playerNameMatchesAnyFilter(
            nameFilters,
        )
    }

    private fun Player.playerNameMatchesAnyFilter(nameFilters: List<String>) =
        nameFilters.any { this.name.contains(it, true) }
}
