package br.com.murilocorreiab.sleepermanager.domain.roster.entity

import br.com.murilocorreiab.sleepermanager.domain.league.entity.League
import br.com.murilocorreiab.sleepermanager.domain.league.entity.LeagueFactory
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Player
import br.com.murilocorreiab.sleepermanager.domain.player.entity.PlayerFactory

object RosterProducer {
    fun build(
        players: List<Player> = arrayListOf(PlayerFactory.build()),
        ownerId: String = "ownerId",
        id: String = "id",
        league: League = LeagueFactory.build()
    ) = Roster(id = id, ownerId = ownerId, players = players, league = league)
}
