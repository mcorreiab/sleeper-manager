package br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity

object SettingsResponseProducer {
    fun build(bestBall: Int? = null) = SettingsResponse(bestBall = bestBall)
}
