package br.com.murilocorreiab.sleepermanager.dataprovider.player.http

import com.fasterxml.jackson.annotation.JsonProperty

data class PlayerResponse(
    @JsonProperty("player_id") val playerId: String,
    @JsonProperty("full_name") val fullName: String?,
    @JsonProperty("first_name") val firstName: String,
    @JsonProperty("last_name") val lastName: String,
    @JsonProperty("injury_status") val injuryStatus: String?,
    val position: String,
    val team: String
)
