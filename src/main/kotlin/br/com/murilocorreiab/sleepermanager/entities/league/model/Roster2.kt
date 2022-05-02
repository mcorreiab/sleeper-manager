package br.com.murilocorreiab.sleepermanager.entities.league.model

import br.com.murilocorreiab.sleepermanager.entities.player.Play
import br.com.murilocorreiab.sleepermanager.entities.player.RawPlayer

data class Roster2(val id: String, val ownerId: String?, val players: List<Play>, val starters: List<RawPlayer>)
