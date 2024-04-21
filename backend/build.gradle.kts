val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val ktormVersion: String by  project

plugins {
    application
    kotlin("jvm") version "1.9.23"
    id("io.ktor.plugin") version "2.3.9"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.4.10"
}

group = "ru.itech.sno_api."
version = "0.0.1"

application {
    mainClass.set("ru.itech.sno_api.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    // ktor-server
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-cio:$ktor_version")

    implementation ("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.3")

    //ktor-client
    implementation ("io.ktor:ktor-client-core:$ktor_version")
    implementation ("io.ktor:ktor-client-cio:$ktor_version")
    implementation ("io.ktor:ktor-client-json:$ktor_version")
    implementation ("io.ktor:ktor-client-serialization:$ktor_version")

    // Для работы с БД mysql
    implementation("mysql:mysql-connector-java:8.0.27")
    implementation("org.jetbrains.exposed:exposed-core:0.37.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.37.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.37.1")
    runtimeOnly("com.mysql:mysql-connector-j")

    // auth
    implementation("com.auth0:java-jwt:3.18.2")
    implementation("org.mindrot:jbcrypt:0.4")


    // документация
    implementation("io.swagger.codegen.v3:swagger-codegen-generators:1.0.36")
    implementation("io.ktor:ktor-server-openapi:$ktor_version")
    implementation("io.ktor:ktor-server-swagger:$ktor_version")


    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
