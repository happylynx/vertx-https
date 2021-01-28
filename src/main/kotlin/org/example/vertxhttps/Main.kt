package org.example.vertxhttps

import io.vertx.core.Vertx
import io.vertx.core.http.HttpServerOptions
import io.vertx.core.net.JksOptions
import java.nio.file.Path

class Main {
    companion object {

        private fun readKeystorePassword(keystorePath: String): String {
            println("Enter keystore (keystorePath) password:")
            return System.`in`.reader().buffered().readLine().trim()
        }

        @JvmStatic
        fun main(args: Array<String>) {
            val keystorePath = Path.of("keystore.jks").toAbsolutePath().toString()
            val keystorePassword = readKeystorePassword(keystorePath)
            val options = HttpServerOptions()
                .setSsl(true)
                .setKeyStoreOptions(JksOptions().setPath(keystorePath).setPassword(keystorePassword))
            val httpsPort: Int = 443
            val serverFuture = Vertx.vertx().createHttpServer(options)
                .requestHandler { request -> request.response().end("hello") }
                .listen(httpsPort)
            serverFuture
                .onSuccess { server -> println("Server is listening at port ${server.actualPort()}") }
                .onFailure { error ->
                    Vertx.currentContext().owner().close()
                    throw error
                }
        }
    }
}