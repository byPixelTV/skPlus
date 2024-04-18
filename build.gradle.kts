plugins {
    kotlin("jvm") version "1.9.23"
    id("io.papermc.paperweight.userdev") version "1.5.15"
    id("xyz.jpenilla.run-paper") version "1.1.0"
}

group = "de.bypixeltv"
version = "1.1"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        url = uri("https://repo.skriptlang.org/releases")
    }
}

dependencies {
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")
    implementation("dev.jorel", "commandapi-bukkit-shade", "9.3.0")
    implementation("dev.jorel", "commandapi-bukkit-kotlin", "9.3.0")
    implementation("net.axay:kspigot:1.20.3")
    implementation("com.github.SkriptLang:Skript:2.8.4")
    implementation("org.json:json:20240303")
    compileOnly("com.github.koca2000:NoteBlockAPI:1.6.2")
}

sourceSets {
    getByName("main") {
        java {
            srcDir("src/main/java")
        }
        kotlin {
            srcDir("src/main/kotlin")
        }
    }
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(17)
        options.compilerArgs.add("-Xlint:deprecation")
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}

kotlin {
    jvmToolchain(17)
}