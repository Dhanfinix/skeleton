plugins {
    id("maven-publish")
    id("signing")
    alias(libs.plugins.vanniktech)
}

// 1. Tell the plugin we are doing a "manual" publication
// We don't use 'mavenPublishing' for the logic because it expects a component
// instead we use the standard 'publishing' block and Vanniktech's signing helper.

publishing {
    publications {
        register<MavenPublication>("maven") {
            groupId = "io.github.dhanfinix"
            artifactId = "skeleton"
            version = "1.0.0"

            // 2. Point to your AAR file
            // Make sure the path is correct relative to this build.gradle
            artifact(file("skeleton.aar"))

            pom {
                name.set("Skeleton")
                description.set("Republished version of unmaintained ethanhua skeleton library")
                url.set("https://github.com/Dhanfinix/skeleton/")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("dhanfinix")
                        name.set("Ramdhan")
                        email.set("muhammadramdhan541@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:github.com/Dhanfinix/skeleton.git")
                    developerConnection.set("scm:git:ssh://github.com/Dhanfinix/skeleton.git")
                    url.set("https://github.com/Dhanfinix/skeleton/")
                }
            }
        }
    }
}

// 3. Configure where to publish (Maven Central)
mavenPublishing {
    // This helper still sets up the Central Portal credentials/host for you
    publishToMavenCentral(automaticRelease = true)

    // This signs whatever publications we just registered above
    signAllPublications()
}

val emptyJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources") // One for sources
}

val emptyJavadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc") // One for javadoc
}

// Update the publication block above to include these:
publishing {
    publications {
        named<MavenPublication>("maven") {
            artifact(emptyJar)
            artifact(emptyJavadocJar)
        }
    }
}