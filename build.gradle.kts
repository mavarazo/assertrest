import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    jacoco
    `java-library`
    `maven-publish`
    id("org.springframework.boot") version "3.2.0"
    id("org.sonarqube") version "4.4.1.3373"
    id("pl.allegro.tech.build.axion-release") version "1.18.9"
}

scmVersion {
    versionCreator("versionWithBranch")
}

group = "com.github.mavarazo"
version = scmVersion.version

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

tasks.bootJar {
    enabled = false
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

sonar {
    properties {
        property("sonar.projectKey", "mavarazo_assertrest")
        property("sonar.organization", "mavarazo")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

dependencies {
    annotationProcessor(platform(SpringBootPlugin.BOM_COORDINATES))
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    testAnnotationProcessor(platform(SpringBootPlugin.BOM_COORDINATES))

    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")

    implementation("org.springframework:spring-web")
    implementation("org.springframework.boot:spring-boot-starter-test")

    testAnnotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-security")
}
