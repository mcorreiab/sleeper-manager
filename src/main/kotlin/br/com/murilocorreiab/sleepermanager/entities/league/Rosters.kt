package br.com.murilocorreiab.sleepermanager.entities.league

import br.com.murilocorreiab.sleepermanager.entities.league.model.Roster2

fun List<Roster2>.getOfUser(userId: String) = this.filter { it.ownerId == userId }
