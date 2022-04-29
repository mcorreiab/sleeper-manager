package br.com.murilocorreiab.sleepermanager.entities.league

class Leagues(private val leagues: List<League>) {
    fun filterOutBestBallLeagues(): List<League> = leagues.filterNot { it.isBestBall }
}
