package br.com.murilocorreiab.sleepermanager.entities.league

import br.com.murilocorreiab.sleepermanager.entities.league.model.League

fun List<League>.filterOutBestBallLeagues() = this.filterNot { it.isBestBall }
