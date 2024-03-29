package br.com.murilocorreiab.sleepermanager.entities.player

object PlayerFactory {
    fun build(
        id: String = "playerId",
        name: String = "playerName",
        injuryStatus: PlayerStatus = PlayerStatus.OUT,
        position: String = "position",
        team: Team = Team.GB,
    ) = Player(id, name, injuryStatus.status, position, team)
}
