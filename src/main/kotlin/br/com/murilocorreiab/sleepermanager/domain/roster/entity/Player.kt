package br.com.murilocorreiab.sleepermanager.domain.roster.entity

data class Player(val id: String, val name: String, val injuryStatus: PlayerStatus, val isStarter: Boolean)
