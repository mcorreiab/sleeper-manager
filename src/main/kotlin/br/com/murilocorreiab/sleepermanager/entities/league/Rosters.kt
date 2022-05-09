package br.com.murilocorreiab.sleepermanager.entities.league

import br.com.murilocorreiab.sleepermanager.entities.league.model.Roster

fun List<Roster>.getOfUser(userId: String) = this.filter { it.ownerId == userId }
