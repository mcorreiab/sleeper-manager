package br.com.murilocorreiab.sleepermanager.dataprovider.config

import com.zaxxer.hikari.HikariDataSource
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Value
import java.net.URI
import javax.inject.Singleton
import javax.sql.DataSource

@Factory
class DatasourceConfig {

    @Value("\$datasources.default.url")
    lateinit var url: String

    @Singleton
    @Replaces(DataSource::class)
    fun dataSource(): DataSource {
        val dbUri = URI(url)
        val username = dbUri.userInfo.split(":")[0]
        val password: String = dbUri.userInfo.split(":")[1]
        val dbUrl = "jdbc:postgresql://${dbUri.host}:${dbUri.port}${dbUri.path}?sslmode=require"

        val dataSource = HikariDataSource()
        dataSource.jdbcUrl = dbUrl
        dataSource.username = username
        dataSource.password = password
        return dataSource
    }
}
