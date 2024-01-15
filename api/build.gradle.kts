import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-mustache")
	implementation("org.springframework.boot:spring-boot-starter-web")

	//kotlin
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	//annotiaion
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	//string
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
}