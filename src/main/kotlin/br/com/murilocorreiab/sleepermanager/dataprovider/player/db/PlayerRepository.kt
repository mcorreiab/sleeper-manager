package br.com.murilocorreiab.sleepermanager.dataprovider.player.db

import br.com.murilocorreiab.sleepermanager.dataprovider.player.db.entity.PlayerDb
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository

@Repository
interface PlayerRepository : ReactiveStreamsCrudRepository<PlayerDb, String>
