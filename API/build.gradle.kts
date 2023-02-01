repositories {
    mavenCentral()
}

val nettyVersion: String by project.extra

dependencies {
    implementation("org.jetbrains:annotations:24.0.0")
    compileOnly("io.netty:netty-all:${nettyVersion}")
}

