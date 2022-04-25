package br.com.murilocorreiab.sleepermanager.domain.roster

import br.com.murilocorreiab.sleepermanager.domain.league.entity.LeagueFactory
import br.com.murilocorreiab.sleepermanager.domain.player.entity.Play
import br.com.murilocorreiab.sleepermanager.domain.player.entity.RawPlayer
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster2
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.RosterFactory2
import br.com.murilocorreiab.sleepermanager.domain.roster.gateway.RosterGateway2
import io.mockk.every

class LeagueMockFactory(private val id: String, private val rosterGateway: RosterGateway2) {
    private val rosters: MutableList<Roster2> = mutableListOf()

    fun newRoster(data: NewRosterData): LeagueMockFactory {
        rosters.add(
            RosterFactory2.build(
                id = data.id,
                players = data.players,
                ownerId = data.ownerId,
                starters = data.starters,
            ),
        )

        return this
    }

    fun create(): LeagueMockFactory {
        every { rosterGateway.findRostersOfLeague(id) } returns rosters.map { it.rawPlayers() }
        return this
    }

    fun getLeague() = LeagueFactory.build(id = id)

    fun getRosterById(id: String) = rosters.first { it.id == id }

    data class NewRosterData(
        val id: String,
        val players: List<Play>,
        val ownerId: String,
        val starters: List<String>
    )

    private fun Roster2.rawPlayers() = copy(players = players.rawPlayers())
    private fun List<Play>.rawPlayers() = map { RawPlayer(it.id) }
}
