package loadrover.api.io.utils

import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

object PathUtils {
    private val projectRootDir = Paths.get(
        Objects.requireNonNull(
            javaClass.getResource("gatling.conf"),
            "Couldn't locate gatling.conf"
        ).toURI()).parent.parent.parent.parent

    private val gradleBuildDirectory = projectRootDir.resolve("build")
    private val gradleSrcDirectory = projectRootDir.resolve("src").resolve("main")

    val gradleSourcesDirectory: Path = gradleSrcDirectory.resolve("kotlin")
    val gradleResourcesDirectory: Path = gradleSrcDirectory.resolve("resources")
    val gradleBinariesDirectory: Path = gradleBuildDirectory.resolve("classes").resolve("kotlin").resolve("main")
    val resultsDirectory: Path = gradleBuildDirectory.resolve("reports").resolve("gatling")
}