package br.com.murilocorreiab.sleepermanager.adapters.user

import com.fasterxml.jackson.annotation.JsonProperty

data class UserResponse(
    @JsonProperty("user_id") val userId: String
)
