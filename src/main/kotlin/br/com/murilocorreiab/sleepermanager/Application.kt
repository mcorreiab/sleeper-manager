package br.com.murilocorreiab.sleepermanager

import io.micronaut.runtime.Micronaut.build
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info

@Suppress("SpreadOperator")
@OpenAPIDefinition(
    info = Info(
        title = "Sleeper Manager",
        version = "1.1.0",
        description = "APIs that intend to easy the life of people playing fantasy on sleeper"
    )
)
class Application {
    fun start(args: Array<String>) {
        build().args(*args).defaultEnvironments("dev")
            .packages("br.com.murilocorreia.sleepermanager").start()
    }
}

fun main(args: Array<String>) {
    Application().start(args)
}
