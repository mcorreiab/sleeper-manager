package br.com.murilocorreiab.sleepermanager.domain.league.entity

import br.com.murilocorreiab.sleepermanager.entities.league.League
import br.com.murilocorreiab.sleepermanager.entities.league.PointsByReception

object LeagueFactory {
    fun build(
        name: String = "league",
        id: String = "120",
        size: Int = 10,
        avatar: String = "avatar",
        pointsByReception: PointsByReception = PointsByReception.STANDARD,
        isBestBall: Boolean = false,
    ) = League(
        name = name,
        id = id,
        size = size,
        avatar = avatar,
        pointsByReception = pointsByReception,
        isBestBall = isBestBall,
    )
}
