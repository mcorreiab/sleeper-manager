package br.com.murilocorreiab.sleepermanager.domain.roster.entity

import br.com.murilocorreiab.sleepermanager.domain.league.entity.League
import br.com.murilocorreiab.sleepermanager.domain.league.entity.LeagueProducer
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerProducer

data class RosterProducer(
    val players: List<Player> = arrayListOf(PlayerProducer.build()),
    val ownerId: String = "ownerId",
    val id: String = "id",
    val league: League = LeagueProducer().build()
) {
    fun build() = Roster(id = id, ownerId = ownerId, players = players, league = league)
}
