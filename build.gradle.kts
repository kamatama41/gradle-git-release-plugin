import com.github.kamatama41.gradle.ghrelease.GitHubReleaseExtension

buildscript {
    val kotlinVersion = "1.1.0"
    extra["kotlinVersion"] = kotlinVersion
    repositories {
        jcenter()
        maven { setUrl("http://kamatama41.github.com/maven-repository/repository") }
        maven { setUrl("./build/ghrelease/repository") }
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.github.kamatama41:gradle-ghrelease-plugin:0.1.0-RC1")
    }
}

apply {
    plugin("idea")
    plugin("kotlin")
    plugin("com.github.kamatama41.ghrelease")
}

repositories {
    jcenter()
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}

val kotlinVersion: String by extra
dependencies {
    compile(gradleApi())
    compile("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    compile("org.ajoberstar:gradle-git-publish:0.1.1-rc.1")
    compile("net.researchgate:gradle-release:2.5.0")
    testCompile("junit:junit:4.12")
}

configure<GitHubReleaseExtension> {
    groupId = "com.github.kamatama41"
    repoUri = "git@github.com:kamatama41/maven-repository.git"
}
