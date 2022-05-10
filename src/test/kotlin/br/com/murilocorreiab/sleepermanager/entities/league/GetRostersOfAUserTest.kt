package br.com.murilocorreiab.sleepermanager.entities.league

import br.com.murilocorreiab.sleepermanager.entities.league.model.Roster
import br.com.murilocorreiab.sleepermanager.entities.league.model.RosterFactory
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class GetRostersOfAUserTest {

    private val userId = "userId"

    @Test
    fun `should get the rosters of a user with success`() {
        // Arrange
        val userRoster1 = RosterFactory.build(id = "1", ownerId = userId)
        val userRoster2 = RosterFactory.build(id = "2", ownerId = userId)

        // Act
        val actual =
            listOf(
                userRoster1,
                userRoster2,
                RosterFactory.build(id = "3", ownerId = "otherUserId"),
            ).getOfUser(userId)

        // Assert
        Assertions.assertThat(actual).containsExactly(userRoster1, userRoster2)
    }

    @ParameterizedTest
    @MethodSource("noUserRostersInputData")
    fun `when all theres no user rosters should return empty list`(rosters: List<Roster>) {
        // Act
        val actual = rosters.getOfUser(userId)

        // Assert
        Assertions.assertThat(actual).isEmpty()
    }

    companion object {
        @JvmStatic
        fun noUserRostersInputData(): List<List<Roster>> = listOf(
            emptyList(),
            listOf(
                RosterFactory.build(id = "1", ownerId = "otherUserId"),
                RosterFactory.build(id = "2", ownerId = "otherUserId"),
            ),
        )
    }
}
