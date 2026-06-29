package com.aienglish.core.network.di

import com.aienglish.core.network.client.GeminiClient
import com.aienglish.core.network.client.NovitaClient
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val coreNetworkModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true; isLenient = true })
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 60_000L
                connectTimeoutMillis = 15_000L
                socketTimeoutMillis = 60_000L
            }
            install(Logging) {
                level = LogLevel.NONE
            }
        }
    }
    single { GeminiClient(get()) }
    single { NovitaClient(get()) }
}
