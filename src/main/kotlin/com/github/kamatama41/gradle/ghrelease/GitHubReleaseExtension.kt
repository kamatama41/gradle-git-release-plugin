package com.github.kamatama41.gradle.ghrelease

import org.gradle.api.Project

open class GitHubReleaseExtension(project: Project) {
    // For Maven artifact
    lateinit var groupId: String
    var artifactId = project.rootProject.name

    // For Maven repository (Git)
    lateinit var repoUri: String
    var repoDir = project.file("${project.buildDir}/ghrelease")
    var repoBranch = "gh-pages"
    var repoPath = "repository"

    // For release
    var releaseBranch = "master"
}
