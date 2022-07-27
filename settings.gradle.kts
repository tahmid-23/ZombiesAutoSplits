pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.minecraftforge.net") {
            name = "Forge"
        }
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        maven("https://maven.architectury.dev/") {
            name = "Architectury"
        }
        maven("https://repo.essential.gg/repository/maven-public/") {
            name = "Essential"
        }
    }
}

rootProject.name = "ZombiesAutoSplits"