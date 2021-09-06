package br.com.murilocorreiab.sleepermanager.dataprovider.league.http.entity

object ScoringSettingsResponseProducer {
    fun build(rec: Double = 0.0) = ScoringSettingsResponse(rec)
}
