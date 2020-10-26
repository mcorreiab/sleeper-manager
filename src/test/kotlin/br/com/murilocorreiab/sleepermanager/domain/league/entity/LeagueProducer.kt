package br.com.murilocorreiab.sleepermanager.domain.league.entity

data class LeagueProducer(val name: String = "league", val id: Long = 120, val size: Int = 10) {
    fun build() = League(name = name, id = id, size = size)
}
