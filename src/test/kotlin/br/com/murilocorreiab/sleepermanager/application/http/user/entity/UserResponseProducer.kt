package br.com.murilocorreiab.sleepermanager.application.http.user.entity

data class UserResponseProducer(
        var username: String = "username",
        var userId: Long = 2392
) {
    fun build() = UserResponse(username, userId)
}