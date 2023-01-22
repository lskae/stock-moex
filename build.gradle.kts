import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.liquibase.gradle") version "2.0.3"
    id("org.springframework.boot") version "2.6.9"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    //https://habr.com/ru/company/haulmont/blog/572574/ особенности работы kotlin и jpa
    id("org.jetbrains.kotlin.jvm") version "1.8.0"
    id("org.jetbrains.kotlin.plugin.spring") version "1.8.0"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.8.0"

    id("maven-publish")
    id("jacoco")
    id("org.jetbrains.kotlin.plugin.allopen") version "1.8.0"
}

allOpen {
    annotations(
        "javax.persistence.Entity",
        "javax.persistence.MappedSuperclass",
        "javax.persistence.Embedabble")
}

group = "line"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_11

extra["springCloudVersion"] = "2021.0.3"

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("com.github.ben-manes.caffeine:caffeine:2.8.8")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("javax.persistence:javax.persistence-api:2.2")
    runtimeOnly("org.springframework.boot:spring-boot-starter-data-rest:2.6.2")
    implementation("org.postgresql:postgresql:42.5.1")
    implementation ("org.liquibase:liquibase-core:4.7.1")

    liquibaseRuntime ("org.liquibase:liquibase-core:4.7.1")
    liquibaseRuntime ("org.postgresql:postgresql:42.5.1")
    liquibaseRuntime ("org.liquibase:liquibase-groovy-dsl:3.0.2")
    liquibaseRuntime ("info.picocli:picocli:4.6.2")
    testImplementation("org.liquibase:liquibase-core:4.7.1")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.github.tomakehurst:wiremock-jre8")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
    testImplementation("org.awaitility:awaitility:4.1.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed", "standardOut", "standardError")
    }
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
    reports {
        xml.required.set(true)
    }
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    this.archiveFileName.set("${project.name}-${project.version}.jar")
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

liquibase {
    activities.register("main") {
        this.arguments = mapOf(
            "logLevel" to "info",
            "changeLogFile" to "src/main/resources/db.changelog.xml",
            "url" to "jdbc:postgresql://localhost/dbName",
            "username" to "userName",
            "password" to "secret")
    }
//    runList = "main"
}

tasks.register("dev") {
    // depend on the liquibase status task
    dependsOn("update")
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            groupId = "${project.group}"
            artifactId = project.name
            version = "${project.version}"
            from(components["java"])
        }
    }
}

