plugins {
    application
}

group = "org.music"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("javazoom:jlayer:1.0.1")
    implementation("org.apache.xmlgraphics:batik-all:1.16")

    implementation("com.mpatric:mp3agic:0.9.1")
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    implementation("com.fasterxml.jackson.core:jackson-core:2.13.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("com.google.http-client:google-http-client-jackson2:1.41.6")
    implementation("com.google.http-client:google-http-client-apache-v2:1.41.5")
    implementation("com.google.auth:google-auth-library-oauth2-http:1.11.0")
    implementation("com.google.api-client:google-api-client:1.33.2")
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.33.3")
    implementation("com.google.apis:google-api-services-youtube:v3-rev222-1.25.0")
    implementation("com.google.apis:google-api-services-youtube:v3-rev20230713-1.32.1")

    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("ch.qos.logback:logback-classic:1.4.12")
    implementation("ch.qos.logback:logback-core:1.4.6")
    implementation("org.mongodb:mongodb-driver-sync:5.2.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("se.michaelthelin.spotify:spotify-web-api-java:6.5.4")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-Xlint:deprecation")
}

tasks.withType<JavaExec> {
    // Loại bỏ cấu hình liên quan đến JavaFX
}