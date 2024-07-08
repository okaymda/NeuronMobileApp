package ru.diplom.projectYolo.data.web

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

private const val yourApi = ""

internal val webDi = DI.Module(
    name = "webDi",
    init = {
        if (yourApi.isEmpty()){
            throw Exception("НАПИШИ СВОЙ IP В ПЕРЕМЕННУЮ yourApi В ru/diplom/projectYolo/data/web/WebDi.kt")
        }
        bind<HttpClient>() with singleton {
            HttpClient(CIO){
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    })
                }
                defaultRequest {
                    url {
                        protocol = URLProtocol.HTTP
                        host = yourApi
                        port = 5000
                    }
                }
                install(Logging) {
                    logger = Logger.ANDROID
                    level = LogLevel.HEADERS
                }
            }
        }
    }
)