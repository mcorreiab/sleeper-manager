package br.com.murilocorreiab.sleepermanager.application.http.user.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class UserResponse(
        val username: String,
        @JsonProperty("user_id") val userId: Long
)
