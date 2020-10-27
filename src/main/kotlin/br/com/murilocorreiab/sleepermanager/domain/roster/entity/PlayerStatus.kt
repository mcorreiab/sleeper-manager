package br.com.murilocorreiab.sleepermanager.domain.roster.entity

enum class PlayerStatus(private val status: String) {
    ACTIVE("active"), OUT("out"), IR("ir");

    companion object {
        fun parsePlayerStatus(status: String) = values().first { status == it.status }
    }
}
