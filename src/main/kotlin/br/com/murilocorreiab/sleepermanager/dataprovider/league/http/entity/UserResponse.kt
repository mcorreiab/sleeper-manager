package br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class UserResponse(
    val username: String,
    @JsonProperty("user_id") val userId: Long
)
