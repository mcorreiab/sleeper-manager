package br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity

import br.com.murilocorreiab.sleepermanager.adapters.league.ScoringSettingsResponse

object ScoringSettingsResponseProducer {
    fun build(rec: Double = 0.0) = ScoringSettingsResponse(rec)
}
