pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()

        maven("https://maven.minecraftforge.net") {
            name = "Forge"
        }

        maven("https://jitpack.io/") {
            name = "Jitpack"
        }
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "net.minecraftforge.gradle.forge") {
                useModule("com.github.asbyth:ForgeGradle:${requested.version}")
            }
        }
    }
}

rootProject.name = "ZombiesAutoSplits"