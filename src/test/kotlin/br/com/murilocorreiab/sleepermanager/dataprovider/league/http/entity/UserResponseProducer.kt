package br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity

object UserResponseProducer {
    fun build(userId: String = "2392") = UserResponse(userId)
}
