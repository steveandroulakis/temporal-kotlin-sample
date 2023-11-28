plugins {
    kotlin("jvm") version "1.9.0"
    application
}

group = "me.steveandroulakis"
version = "1.0-SNAPSHOT"
val javaSDKVersion = "1.22.3"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("io.temporal:temporal-kotlin:$javaSDKVersion")
    implementation("io.temporal:temporal-sdk:$javaSDKVersion")
    implementation("io.temporal:temporal-opentracing:$javaSDKVersion")
    implementation("io.temporal:temporal-testing:$javaSDKVersion")
    testImplementation("io.temporal:temporal-testing:$javaSDKVersion")

    // SLF4J API
    implementation("org.slf4j:slf4j-api:1.7.30") // Use the latest version
    // Logback Classic
    implementation("ch.qos.logback:logback-classic:1.2.3") // Use the latest version
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}