import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar


repositories {
    mavenCentral()
    maven("https://repo.viaversion.com/")
    maven("https://repo.aikar.co/nexus/content/groups/aikar/")
    // spigotmc-repo
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    // sonatype
    maven("https://oss.sonatype.org/content/groups/public/")
}

val adventureVersion: String by project.extra
val nettyVersion: String by project.extra

dependencies {
    implementation(project(":API"))

    implementation(platform("net.kyori:adventure-bom:$adventureVersion"))
    implementation("net.kyori:adventure-nbt")

    implementation("co.aikar:minecraft-timings:1.0.4")

    implementation("io.netty:netty-codec:${nettyVersion}")
    compileOnly("com.viaversion:viaversion:4.5.1")

    compileOnly("org.spigotmc:spigot-api:1.19.3-R0.1-SNAPSHOT")

    testCompileOnly("org.spigotmc:spigot-api:1.19.3-R0.1-SNAPSHOT")
}

tasks.getByName<ShadowJar>("shadowJar") {
    relocate("co.aikar.timings.lib", "de.snowii.packeto.timingslib")
}

/**tasks.register<ShadowJar>("testJar") {
classifier = "tests"
from(shadowJar)
from(sourceSets.main.output)
from(sourceSets.test.output)
project.configurations.implementation.isCanBeConsumed = true
configurations = [project.configurations.implementation]
}**/

/** processResources {
val props = [name: name, version: version, description: description]
inputs.properties props
filteringCharset "UTF-8"
filesMatching("plugin.yml") {
expand props
}
}

processTestResources {
val props = [name: name, version: version, description: description]
inputs.properties props
filteringCharset "UTF-8"
filesMatching("plugin.yml") {
expand props
}
} **/
