import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.7"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
}

java {
	sourceCompatibility = JavaVersion.VERSION_17

	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

kotlin {
	jvmToolchain(17)
}

allprojects {
	group = "loadrover.io"
	version = "0.0.1-SNAPSHOT"

	repositories {
		mavenCentral()
	}
}

subprojects {

	apply(plugin = "io.spring.dependency-management")
	apply(plugin = "org.springframework.boot")
	apply(plugin = "org.jetbrains.kotlin.plugin.spring")
	apply(plugin = "kotlin")
	apply(plugin = "java")

	dependencies {
		implementation("org.springframework.boot:spring-boot-starter-mustache")
		implementation("org.springframework.boot:spring-boot-starter-web")

		//kotlin
		implementation("org.jetbrains.kotlin:kotlin-reflect")
		implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

		//annotation
		annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

		//swagger
		implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")

		//string
		implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	}

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs += "-Xjsr305=strict"
			jvmTarget = "17"
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}

	tasks.register("prepareKotlinBuildScriptModel"){
	}

	configurations {
		compileOnly {
			extendsFrom(configurations.annotationProcessor.get())
		}
	}
}

// api
project(":api") {
	tasks.withType<Test> {
		exclude("**/*")
		useJUnitPlatform()
	}
}