package br.com.murilocorreiab.sleepermanager.dataprovider.player.db.entity

import io.micronaut.core.annotation.Introspected
import javax.persistence.Entity
import javax.persistence.Id

@Introspected
@Entity
data class PlayerDb(@Id val id: String, val name: String, val injuryStatus: String, var starter: Boolean)
