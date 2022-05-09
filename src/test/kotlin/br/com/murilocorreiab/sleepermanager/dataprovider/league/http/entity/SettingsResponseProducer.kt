package br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity

import br.com.murilocorreiab.sleepermanager.adapters.league.SettingsResponse

object SettingsResponseProducer {
    fun build(bestBall: Int? = null) = SettingsResponse(bestBall = bestBall)
}
