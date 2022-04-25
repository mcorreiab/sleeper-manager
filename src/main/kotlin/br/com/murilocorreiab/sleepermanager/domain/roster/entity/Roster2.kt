package br.com.murilocorreiab.sleepermanager.domain.roster.entity

import br.com.murilocorreiab.sleepermanager.domain.player.entity.Play
import br.com.murilocorreiab.sleepermanager.domain.player.entity.RawPlayer

data class Roster2(val id: String, val ownerId: String?, val players: List<Play>, val starters: List<RawPlayer>)
