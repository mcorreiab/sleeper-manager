package br.com.murilocorreiab.sleepermanager.dataprovider.league.entity

import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.UserResponse

data class UserResponseProducer(
    var username: String = "username",
    var userId: Long = 2392
) {
    fun build() = UserResponse(username, userId)
}
