import com.github.kamatama41.gradle.gitrelease.GitReleaseExtension
import org.gradle.plugin.devel.GradlePluginDevelopmentExtension

buildscript {
    val kotlinVersion = "1.1.0"
    extra["kotlinVersion"] = kotlinVersion
    repositories {
        jcenter()
        maven { setUrl("http://kamatama41.github.com/maven-repository/repository") }
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.github.kamatama41:gradle-git-release-plugin:+")
    }
}

apply {
    plugin("idea")
    plugin("kotlin")
    plugin("java-gradle-plugin")
    plugin("com.github.kamatama41.git-release")
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
    testCompile(gradleTestKit())
    testCompile("junit:junit:4.12")
    testCompile("org.eclipse.jgit:org.eclipse.jgit.junit:4.5.0.201609210915-r")
}

val localRepoDir: String by extra
configure<GitReleaseExtension> {
    groupId = "com.github.kamatama41"
    repoUri = "git@github.com:kamatama41/maven-repository.git"
    repoDir = file("${System.getProperty("user.home")}/gh-maven-repository")
}

configure<GradlePluginDevelopmentExtension> {
    isAutomatedPublishing = false
}
