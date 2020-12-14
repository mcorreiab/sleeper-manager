package br.com.murilocorreiab.sleepermanager.config

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import io.micronaut.context.ApplicationContext
import io.micronaut.test.extensions.junit5.MicronautJunit5Extension
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

class WireMockExtension : BeforeAllCallback, AfterAllCallback, AfterEachCallback {

    private lateinit var wireMockServer: WireMockServer

    override fun beforeAll(context: ExtensionContext) {
        val micronaut = getMicronautApplicationContext(context)
        val port: Int = micronaut.getRequiredProperty("wiremock.server.port", Int::class.java)

        wireMockServer = WireMockServer(WireMockConfiguration.wireMockConfig().port(port))
        wireMockServer.start()
        WireMock.configureFor("localhost", port)
    }

    private fun getMicronautApplicationContext(extensionContext: ExtensionContext): ApplicationContext =
        extensionContext.getStore(ExtensionContext.Namespace.create(MicronautJunit5Extension::class.java))
            .get(ApplicationContext::class.java) as ApplicationContext

    override fun afterAll(context: ExtensionContext?) {
        if (wireMockServer.isRunning) {
            wireMockServer.stop()
        }
    }

    override fun afterEach(context: ExtensionContext?) {
        wireMockServer.resetAll()
    }
}
