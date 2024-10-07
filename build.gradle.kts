import com.vanniktech.maven.publish.SonatypeHost
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    jacoco
    `java-library`
    id("org.springframework.boot") version "3.2.0"
    id("org.sonarqube") version "4.4.1.3373"
    id("pl.allegro.tech.build.axion-release") version "1.18.9"
    id("com.vanniktech.maven.publish") version "0.29.0"
}

scmVersion {
    versionCreator("versionWithBranch")
}

group = "io.github.mavarazo"
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

tasks.jar {
    enabled = true
    archiveClassifier.set("")
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

mavenPublishing {
    coordinates(
        groupId = group as String,
        artifactId = name,
        version = version as String
    )

    pom {
        name = "assertrest"
        description = "Library to improve test code readability when writing tests with TestRestTemplate."
        url = "https://github.com/mavarazo/assertrest"

        developers {
            developer {
                id = "mavarazo"
                name = "Marco Niederberger"
                email = "marco.niederberger@gmail.com"
                url = "https://mavarazo.github.io/"
            }
        }

        licenses {
            license {
                name = "Apache License"
                url = "https://github.com/mavarazo/assertrest/blob/main/LICENSE"
            }
        }

        scm {
            url = "https://github.com/mavarazo/assertrest"
            connection = "scm:git:https://github.com/mavarazo/assertrest.git"
            developerConnection = "scm:git:git@github.com:mavarazo/assertrest.git"
        }
    }

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
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
