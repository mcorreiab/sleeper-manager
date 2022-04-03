package br.com.murilocorreiab.sleepermanager.domain.roster.entity

import br.com.murilocorreiab.sleepermanager.domain.league.entity.League
import br.com.murilocorreiab.sleepermanager.domain.league.entity.LeagueProducer

object RosterProducer2 {
    fun build(
        players: List<String> = arrayListOf("1"),
        ownerId: String = "ownerId",
        id: String = "id",
        league: League = LeagueProducer.build()
    ) = Roster2(id = id, ownerId = ownerId, players = players, league = league)
}
