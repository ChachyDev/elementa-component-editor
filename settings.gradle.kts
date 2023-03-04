pluginManagement {
    repositories {
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

val modulePrefix = ":elementa-component-editor-"
val platformPrefix = "platform-"

include(modulePrefix + "common")

include(modulePrefix + platformPrefix + "fabric")

project(modulePrefix + "common").projectDir = File(rootDir, "common")

val platformsDir = File(rootDir, "platforms")

project(modulePrefix + platformPrefix + "fabric").projectDir = File(platformsDir, "fabric")