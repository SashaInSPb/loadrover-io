import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("plugin.jpa")
	kotlin("jvm")
}

apply(plugin = "kotlin-jpa")

// 모든 클래스를 open으로 변경
allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

// @Entity, @Embeddable, @MappedSuperClass 가 붙은 클래스에 한해서만 자동으로 기본 생성자 생성
noArg {
	annotation("jakarta.persistence.Entity")
	annotation("com.fasterxml.jackson.annotation.JsonInclude")
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

	//aws
	implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")

	//db
	runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	//thymeleaf
	implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}
repositories {
	mavenCentral()
}
kotlin {
	jvmToolchain(17)
}