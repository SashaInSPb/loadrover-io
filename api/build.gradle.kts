import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

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

	//html parsing
	implementation("org.jsoup:jsoup:1.15.3")

	//db
	runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}