import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.7"
	id("io.spring.dependency-management") version "1.1.4"
	id("io.gatling.gradle") version "3.10.3"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
}

apply(plugin = "io.gatling.gradle")

allOpen {
	annotation("com.fasterxml.jackson.annotation.JsonInclude")
}

//noArg {
//	annotation("jakarta.persistence.Entity") // @Entity가 붙은 클래스에 한해서만 no arg 플러그인을 적용
//	annotation("com.fasterxml.jackson.annotation.JsonInclude")
//
//}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-mustache")
	implementation("org.springframework.boot:spring-boot-starter-web")

	//kotlin
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	//annotation
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	//string
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
}
