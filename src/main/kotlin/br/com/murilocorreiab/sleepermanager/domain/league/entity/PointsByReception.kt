package br.com.murilocorreiab.sleepermanager.domain.league.entity

enum class PointsByReception(val points: Double, val text: String) {
    STANDARD(0.0, "Standard"), HALF_PPR(0.5, "Half PPR"), PPR(1.0, "PPR");

    companion object {
        fun getByPoints(points: Double): PointsByReception = values().find { it.points == points } ?: STANDARD
    }
}
