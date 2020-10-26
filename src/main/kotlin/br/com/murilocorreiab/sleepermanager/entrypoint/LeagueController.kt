package br.com.murilocorreiab.sleepermanager.entrypoint

import br.com.murilocorreiab.sleepermanager.domain.league.ListLeaguesUseCase
import br.com.murilocorreiab.sleepermanager.domain.league.entity.League
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking

@Controller("/leagues")
class LeagueController(private val listLeaguesUseCase: ListLeaguesUseCase) {

    @Get("/username/{username}")
    fun getLeaguesByUsername(@PathVariable username: String): Flow<League> = runBlocking {
        listLeaguesUseCase.findUserLeagues(username)
    }
}
