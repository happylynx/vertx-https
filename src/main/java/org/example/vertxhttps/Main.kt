package org.example.vertxhttps

import io.vertx.core.Vertx
import io.vertx.core.http.HttpServerOptions

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val options = HttpServerOptions()
                .setSsl(true)
            val serverFuture = Vertx.vertx().createHttpServer(options)
                .requestHandler { request -> request.response().end("hello") }
                .listen()
            serverFuture
                .onSuccess { server -> println("Server is listening at port ${server.actualPort()}") }
                .onFailure { error ->
                    Vertx.currentContext().owner().close()
                    throw error
                }
        }
    }
}