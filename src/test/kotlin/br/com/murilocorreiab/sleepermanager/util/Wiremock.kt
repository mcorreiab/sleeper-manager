package br.com.murilocorreiab.sleepermanager.util

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import io.micronaut.context.annotation.Value
import javax.inject.Singleton

@Singleton
class Wiremock(@Value("\${wiremock.server.port}") port: Int) {
    private val wireMockServer = WireMockServer(WireMockConfiguration.wireMockConfig().port(port))

    init {
        wireMockServer.start()
        WireMock.configureFor("localhost", port)
    }
}
