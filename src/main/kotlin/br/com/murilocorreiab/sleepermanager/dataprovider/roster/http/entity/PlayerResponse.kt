package br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class PlayerResponse(
    @JsonProperty("player_id") val playerId: String,
    @JsonProperty("search_first_name") val firstName: String,
    @JsonProperty("search_last_name") val lastName: String,
    @JsonProperty("injury_status") val injuryStatus: String
)
