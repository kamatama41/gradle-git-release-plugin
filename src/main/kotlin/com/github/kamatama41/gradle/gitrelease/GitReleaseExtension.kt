package com.github.kamatama41.gradle.gitrelease

import org.gradle.api.Project

open class GitReleaseExtension(project: Project) {
    // For Maven artifact
    lateinit var groupId: String
    var artifactId = project.rootProject.name

    // For Maven repository (Git)
    lateinit var repoUri: String
    var repoDir = project.file("${project.buildDir}/git-release")
    var repoBranch = "gh-pages"
    var repoPath = "repository"

    // For release
    var releaseBranch = "master"
}
