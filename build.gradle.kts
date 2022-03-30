plugins {
    java
    id("com.github.johnrengelman.shadow") version "6.1.0"
    id ("net.minecraftforge.gradle.forge") version "6f53277"
}

version = "1.0"
group = "com.github.tahmid_23"

minecraft {
    version = "1.8.9-11.15.1.2318-1.8.9"
    runDir = "run"
    mappings = "stable_22"
    makeObfSourceJar = false
}

val include: Configuration by configurations.creating
configurations.implementation.get().extendsFrom(include)

tasks.compileJava {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"

    options.encoding = "UTF-8"
}

tasks.processResources {
    inputs.property("version", version)
    inputs.property("mcversion", minecraft.version)

    from(sourceSets.main.get().resources.srcDirs) {
        include("mcmod.info")

        expand("version" to version, "mcversion" to minecraft.version)
    }

    from(sourceSets.main.get().resources.srcDirs) {
        exclude("mcmod.info")
    }
}

sourceSets {
    main {
        output.setResourcesDir(java.outputDir)
    }
}

tasks.shadowJar {
    archiveClassifier.set("")
    archiveBaseName.set("ZombiesAutoSplits")
    configurations = listOf(include)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

reobf {
    register("shadowJar") {}
}

tasks.jar {
    dependsOn(tasks.shadowJar)
}
tasks.jar.get().enabled = false