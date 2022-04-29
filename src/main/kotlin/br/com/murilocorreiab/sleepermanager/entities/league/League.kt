package br.com.murilocorreiab.sleepermanager.entities.league

data class League(
    val name: String,
    val id: String,
    val size: Int,
    val avatar: String?,
    val pointsByReception: PointsByReception,
    val isBestBall: Boolean,
)
