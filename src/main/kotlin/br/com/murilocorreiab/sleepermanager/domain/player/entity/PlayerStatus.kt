package br.com.murilocorreiab.sleepermanager.domain.player.entity

enum class PlayerStatus(val status: String) {
    ACTIVE("Active"), OUT("Out"), IR("IR"), QUESTIONABLE("Questionable"), DOUBTFUL("Doubtful");
}
