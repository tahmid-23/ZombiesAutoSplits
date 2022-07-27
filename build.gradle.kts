import dev.architectury.pack200.java.Pack200Adapter

@Suppress(
    "DSL_SCOPE_VIOLATION",
    "MISSING_DEPENDENCY_CLASS",
    "UNRESOLVED_REFERENCE_WRONG_RECEIVER",
    "FUNCTION_CALL_EXPECTED"
)
plugins {
    java
    id("gg.essential.loom") version "0.10.0.4"
    id("dev.architectury.architectury-pack200") version "0.1.3"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

version = "1.0"
group = "com.github.tahmid_23"

loom {
    runConfigs {
        named("client") {
            ideConfigGenerated(true)
        }
    }
    forge {
        pack200Provider.set(Pack200Adapter())
    }
    setupRemappedVariants.set(false)
}

val include: Configuration by configurations.creating
configurations.implementation.get().extendsFrom(include)

dependencies {
    minecraft("com.mojang:minecraft:1.8.9")
    mappings("de.oceanlabs.mcp:mcp_stable:22-1.8.9")
    forge("net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9")
}

tasks.compileJava {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"

    options.encoding = Charsets.UTF_8.name()
    options.release.set(8)
}

tasks.processResources {
    inputs.property("version", version)
    inputs.property("mcversion", "1.8.9")

    from(sourceSets.main.get().resources.srcDirs) {
        include("mcmod.info")

        expand("version" to version, "mcversion" to "1.8.9")
    }

    from(sourceSets.main.get().resources.srcDirs) {
        exclude("mcmod.info")
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.jar.get().enabled = false

tasks.shadowJar {
    archiveClassifier.set("")
    archiveBaseName.set("ZombiesAutoSplits")
    configurations = listOf(include)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.remapJar {
    dependsOn(tasks.shadowJar)
    input.set(tasks.shadowJar.get().archiveFile)
}