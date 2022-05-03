package br.com.murilocorreiab.sleepermanager.entities.league

import br.com.murilocorreiab.sleepermanager.entities.league.model.League
import br.com.murilocorreiab.sleepermanager.entities.league.model.Roster2

data class LeagueWithRosters(val league: League, val rosters: List<Roster2>)
