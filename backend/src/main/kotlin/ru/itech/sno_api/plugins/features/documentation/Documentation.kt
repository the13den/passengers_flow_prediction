package ru.itech.sno_api.plugins.features.documentation

import io.ktor.server.application.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*
import io.swagger.codegen.v3.generators.html.StaticHtmlCodegen

fun Application.configureDocumentationRouting() {
    routing {

        openAPI(path = "/api/v2/openapi", swaggerFile = "documentation.yaml") {
            codegen = StaticHtmlCodegen()
        }

        swaggerUI(path = "/api/v2/swagger", swaggerFile = "documentation.yaml") {
            version = "4.15.5"
        }
    }

}