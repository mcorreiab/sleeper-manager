package br.com.murilocorreiab.sleepermanager.application.entrypoint

import br.com.murilocorreiab.sleepermanager.domain.entity.League
import br.com.murilocorreiab.sleepermanager.domain.usecase.ListLeaguesUseCase
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable

@Controller("/leagues")
class LeagueController(private val listLeaguesUseCase: ListLeaguesUseCase) {

    @Get("/username/{username}")
    fun getLeaguesByUsername(@PathVariable username: String): List<League> {
        return listLeaguesUseCase.findUserLeagues(username)
    }
}