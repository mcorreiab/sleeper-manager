package br.com.murilocorreiab.sleepermanager.domain.roster.entity

import br.com.murilocorreiab.sleepermanager.domain.league.entity.League
import br.com.murilocorreiab.sleepermanager.domain.league.entity.LeagueProducer
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerProducer

object RosterProducer {
    fun build(
        players: List<Player> = arrayListOf(PlayerProducer.build()),
        ownerId: String = "ownerId",
        id: String = "id",
        league: League = LeagueProducer.build()
    ) = Roster(id = id, ownerId = ownerId, players = players, league = league)
}
