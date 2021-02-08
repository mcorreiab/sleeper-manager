package br.com.murilocorreiab.sleepermanager.entrypoint.entity

data class LeagueResponse(
    val name: String,
    val id: String,
    val size: Int,
    val avatar: String?,
    val pointsByReception: String
)
