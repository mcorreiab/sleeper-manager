package br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity

import br.com.murilocorreiab.sleepermanager.adapters.user.UserResponse

object UserResponseProducer {
    fun build(userId: String = "2392") = UserResponse(userId)
}
