plugins {
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.allopen")
	id("io.gatling.gradle") version "3.10.3"
}

apply(plugin = "io.gatling.gradle")

allOpen {
	annotation("com.fasterxml.jackson.annotation.JsonInclude")
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

dependencies {
	// gatling
	gatling ("com.google.code.gson:gson:2.8.9")
	gatlingImplementation ("org.apache.commons:commons-lang3:3.4")
	gatlingRuntimeOnly ("cglib:cglib-nodep:3.2.0")
}
