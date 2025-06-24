plugins {
    `java-library`
    `maven-publish`
    id("org.springframework.boot") version "3.3.3"
    id("io.spring.dependency-management") version "1.1.3"
}

repositories {
    mavenCentral()
    maven("https://repo.spring.io/milestone")
    maven("https://repo.spring.io/snapshot")
}


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    api(libs.org.springframework.boot.spring.boot.starter.data.jpa)
    api(libs.io.jsonwebtoken.jjwt.api)
    api(libs.org.liquibase.liquibase.core)
    api(libs.io.jsonwebtoken.jjwt.gson)
    api(libs.io.jsonwebtoken.jjwt.impl)
    api(libs.io.jsonwebtoken.jjwt.jackson)
    api(libs.org.hibernate.hibernate.validator)
    api(libs.org.mapstruct.mapstruct)
    api(libs.com.zaxxer.hikaricp)
    api(libs.org.springframework.boot.spring.boot.starter.actuator)
    api(libs.org.projectlombok.lombok.mapstruct.binding)
    api(libs.org.springdoc.springdoc.openapi.starter.webmvc.ui)
    api(libs.jakarta.validation.jakarta.validation.api)
    api(libs.org.springframework.boot.spring.boot.starter.web)
    api(libs.org.apache.commons.commons.lang3)
    api(libs.org.springframework.boot.spring.boot.starter.security)
    api(libs.org.springframework.boot.spring.boot.starter.data.redis)
    api(libs.org.springframework.boot.spring.boot.starter.cache)
    api(libs.io.lettuce.lettuce.core)
    api(libs.net.logstash.logback.logstash.logback.encoder)
    api(libs.org.apache.poi.poi.ooxml)
    api(libs.org.xhtmlrenderer.flying.saucer.pdf.itext5)
    api(libs.org.springframework.boot.spring.boot.starter.thymeleaf)
    api(libs.org.springframework.boot.spring.boot.starter.webflux)
    api(libs.org.springframework.boot.spring.boot.starter.quartz)

    runtimeOnly(libs.org.postgresql.postgresql)
    runtimeOnly(libs.io.opentelemetry.instrumentation.opentelemetry.logback.appender.v1.v0)
    runtimeOnly(libs.io.opentelemetry.instrumentation.opentelemetry.logback.mdc.v1.v0)

    testImplementation(libs.org.springframework.boot.spring.boot.starter.test)
    compileOnly(libs.org.projectlombok.lombok)
    compileOnly(libs.jakarta.servlet.jakarta.servlet.api)

    annotationProcessor(libs.org.projectlombok.lombok)
    annotationProcessor(libs.org.mapstruct.mapstruct.processor)
    annotationProcessor(libs.org.projectlombok.lombok.mapstruct.binding)

    testCompileOnly(libs.org.projectlombok.lombok)
    testAnnotationProcessor(libs.org.projectlombok.lombok)
    testAnnotationProcessor(libs.org.mapstruct.mapstruct.processor)
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "health-management"

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.apply {
        encoding = "UTF-8"
        compilerArgs.addAll(
            listOf(
                "-parameters",
                "-Amapstruct.defaultComponentModel=spring",
                "-Amapstruct.unmappedTargetPolicy=IGNORE"
            )
        )
        isFork = true
        options.forkOptions.apply {
            jvmArgs = listOf("-Xmx2g")
        }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()

    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
}

tasks.withType<AbstractCompile>().configureEach {
    inputs.property("java.version", System.getProperty("java.version"))
}

springBoot {
    buildInfo()
    mainClass = "com.example.health_management.HealthManagementApplication"
}

tasks.withType<Javadoc>().configureEach {
    options.encoding = "UTF-8"
}
