package br.com.murilocorreiab.sleepermanager.domain.player.entity

object PlayerProducer {
    fun build(
        id: String = "playerId",
        name: String = "playerName",
        injuryStatus: PlayerStatus = PlayerStatus.OUT,
        starter: Boolean = true,
        position: String = "position",
        team: Team = Team.GB,
    ) = Player(id, name, injuryStatus.status, starter, position, team)
}
