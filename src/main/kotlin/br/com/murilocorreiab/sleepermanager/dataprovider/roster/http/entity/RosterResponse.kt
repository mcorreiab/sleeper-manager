package br.com.murilocorreiab.sleepermanager.dataprovider.roster.http.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class RosterResponse(
    @JsonProperty("roster_id") val rosterId: String,
    val starters: List<String>,
    val players: List<String>,
    @JsonProperty("owner_id") val ownerId: String?
)
