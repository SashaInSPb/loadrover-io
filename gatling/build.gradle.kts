import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.allopen")
	id("io.gatling.gradle") version "3.10.3"
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
	// gatling
	gatling ("com.google.code.gson:gson:2.8.9")
	gatlingImplementation ("org.apache.commons:commons-lang3:3.4")
	gatlingRuntimeOnly ("cglib:cglib-nodep:3.2.0")
}
