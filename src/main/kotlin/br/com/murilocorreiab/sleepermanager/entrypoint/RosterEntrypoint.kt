package br.com.murilocorreiab.sleepermanager.entrypoint

import br.com.murilocorreiab.sleepermanager.domain.roster.RosterService
import br.com.murilocorreiab.sleepermanager.domain.roster.entity.Roster
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking

@Controller("/rosters")
class RosterEntrypoint(private val rosterService: RosterService) {

    @Get("/user/{username}/unavailable")
    fun recoverRosterOfAUser(@PathVariable username: String): Flow<Roster> = runBlocking {
        rosterService.checkForUnavailablePlayers(username)
    }
}
