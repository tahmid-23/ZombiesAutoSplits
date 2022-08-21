@Suppress(
    "DSL_SCOPE_VIOLATION",
    "MISSING_DEPENDENCY_CLASS",
    "UNRESOLVED_REFERENCE_WRONG_RECEIVER",
    "FUNCTION_CALL_EXPECTED"
)
plugins {
    java
    id("fabric-loom") version "0.12.56"
}

base {
    archivesName.set("ZombiesAutoSplits")
}

version = "1.0"
group = "com.github.tahmid_23"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

val includeTransitive: Configuration by configurations.creating {
    exclude("net.fabricmc", "fabric-loader")
    exclude("it.unimi.dsi", "fastutil")
}

repositories {
    maven("https://dl.cloudsmith.io/public/steank-f1g/ethylene/maven/") {
        name = "Ethylene"
    }
    maven("https://jitpack.io/") {
        name = "Jitpack"
        content {
            includeModule("com.github.tahmid-23", "CustomHUD")
        }
    }
    maven("https://api.modrinth.com/maven") {
        name = "Modrinth"
        content {
            includeGroup("maven.modrinth")
        }
    }
    maven("https://maven.shedaniel.me/") {
        name = "Shedaniel"
    }
    maven("https://maven.terraformersmc.com/") {
        name = "TerraformersMC"
    }
}

dependencies {
    minecraft("com.mojang:minecraft:1.18.2")
    mappings("net.fabricmc:yarn:1.18.2+build.4:v2")
    modImplementation("net.fabricmc:fabric-loader:0.14.9")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.58.0+1.18.2")

    modImplementation("me.shedaniel.cloth:cloth-config-fabric:6.2.62") {
        exclude("net.fabricmc.fabric-api")
    }
    modImplementation("com.terraformersmc:modmenu:3.2.3") {
        exclude("net.fabricmc.fabric-api")
    }
    modImplementation("com.github.tahmid-23:CustomHUD:1.18-SNAPSHOT") {
        exclude("net.fabricmc.fabric-api")
    }

    implementation("com.github.steanky:ethylene-yaml:0.12.0")
    includeTransitive("com.github.steanky:ethylene-yaml:0.12.0")

    val resolutionResult = includeTransitive.incoming.resolutionResult
    resolutionResult.allComponents {
        when (val idCopy = id) {
            resolutionResult.root.id -> {
                return@allComponents
            }
            is ModuleComponentIdentifier -> {
                include(idCopy.group, idCopy.module, idCopy.version)
            }
            is ProjectComponentIdentifier -> {
                include(project(idCopy.projectPath))
            }
        }
    }
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${archiveBaseName.get()}"}
    }
}

tasks.remapJar {
    archiveAppendix.set("fabric")
}