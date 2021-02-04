package br.com.murilocorreiab.sleepermanager.dataprovider.league.entity

import br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity.ScoringSettingsResponse

data class ScoringSettingsResponseProducer(val rec: Double = 0.0) {
    fun build() = ScoringSettingsResponse(rec)
}
