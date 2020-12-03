plugins {
    java
    id("fabric-loom") version "0.5-SNAPSHOT"
}

val minecraftVersion = "20w49a"
val yarnVersion = "$minecraftVersion+build.1:v2"
val fabricLoaderVersion = "0.10.8"
val fabricApiVersion = "0.27.2+1.17"
val modmenuVersion = "2.0.0-beta.1+build.2"

group = "com.mumfrey.worldeditcui"
version = "$minecraftVersion+01-SNAPSHOT"

repositories {
    maven(url = "https://maven.enginehub.org/repo") {
        name = "enginehub"
    }
    maven(url = "https://maven.dblsaiko.net/") {
        name = "dblsaiko"
    }
    mavenLocal {
        content {
            includeGroup("com.sk89q.worldedit")
        }
    }
}

val targetVersion = 8
java {
    sourceCompatibility = JavaVersion.toVersion(targetVersion)
    targetCompatibility = sourceCompatibility
}

tasks.withType(JavaCompile::class) {
    if (JavaVersion.current().isJava10Compatible) {
        options.release.set(targetVersion)
    }
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$yarnVersion")
    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
    modImplementation("io.github.prospector:modmenu:$modmenuVersion")

    // modImplementation(include("grondag:frex-events-mc116:1.0.+")!!) // for render event

    // for development
    modRuntime("com.sk89q.worldedit:worldedit-fabric-mc20w48a:7.2.1-SNAPSHOT") {
        exclude(group = "com.google.guava")
        exclude(group = "com.google.code.gson")
        exclude(group = "it.unimi.dsi")
        exclude(group = "org.apache.logging.log4j")
    }
}

tasks.processResources.configure {
    inputs.property("version", project.version)

    from(sourceSets["main"].resources.srcDirs) {
        include("fabric.mod.json")
        expand("version" to project.version)
    }
}
