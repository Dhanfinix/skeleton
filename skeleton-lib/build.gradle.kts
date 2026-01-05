import java.io.FileInputStream
import java.util.Properties

plugins {
    id("maven-publish")
}


configurations.maybeCreate("default")

artifacts {
    add("default", file("skeleton.aar"))
}

val githubProperties = Properties()
val secretFile = rootProject.file("github.properties")

if (secretFile.exists()) {
    githubProperties.load(FileInputStream(secretFile))
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("bar") {
                artifact(file("skeleton.aar")) {
                    extension = "aar"
                }

                groupId = "dhanfinix.republish"
                artifactId = "skeleton"
                version = "1.0.0"
            }
        }

        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/Dhanfinix/skeleton")

                credentials {
                    // Assuming 'githubProperties' is a Map or Properties object defined earlier
                    username = githubProperties["USER_ID"]?.toString() ?: System.getenv("USER_ID")
                    password = githubProperties["ACCESS_TOKEN"]?.toString() ?: System.getenv("ACCESS_TOKEN")
                }
            }
        }
    }
}