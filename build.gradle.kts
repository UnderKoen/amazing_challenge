group "nl.underkoen"
version "1.0-SNAPSHOT"

repositories {
    jcenter()
    maven { url = uri("https://jitpack.io") }
}

plugins {
    java
    id("cup.gradle.cup-gradle-plugin") version "1.2"
    id("org.xbib.gradle.plugin.jflex") version "1.2.1"
    id("org.openjfx.javafxplugin") version "0.0.9"
}

dependencies {
    compile("com.github.UnderKoen.EduLogo:Editor:1.15")
}

apply(plugin = "org.openjfx.javafxplugin")

javafx {
    version = "14"
    modules("javafx.base", "javafx.graphics" , "javafx.controls")
}

tasks.cupCompile {
    dependsOn(tasks.jflex)
}

tasks.jar {
    manifest {
        attributes(
                "Main-Class" to "example.Main"
        )
    }
    from(configurations.runtimeClasspath.get().map {
        if (it.isDirectory) it else zipTree(it)
    })
}
