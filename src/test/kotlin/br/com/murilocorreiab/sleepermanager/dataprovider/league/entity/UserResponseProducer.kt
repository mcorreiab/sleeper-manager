package br.com.murilocorreiab.sleepermanager.dataprovider.league.entity

import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.UserResponse

data class UserResponseProducer(
    var userId: String = "2392"
) {
    fun build() = UserResponse(userId)
}
