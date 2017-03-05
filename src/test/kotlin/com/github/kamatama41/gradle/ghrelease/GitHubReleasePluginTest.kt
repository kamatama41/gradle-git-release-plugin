package com.github.kamatama41.gradle.ghrelease

import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.tasks.bundling.Jar
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class GitHubReleasePluginTest {
    @Rule @JvmField
    val testProjectDir = TemporaryFolder()

    val project: ProjectInternal by lazy {
        val projectDir = testProjectDir.root
        val project = ProjectBuilder.builder().withProjectDir(projectDir).build() as ProjectInternal

        project.pluginManager.apply("com.github.kamatama41.ghrelease")

        val extension = project.extensions.findByName(GitHubReleasePlugin.extensionName) as GitHubReleaseExtension
        extension.groupId = "com.example"
        extension.repoUri = "git@github.com:testUser/testRepo.git"

        project
    }

    @Test
    fun sourceJar() {
        project.evaluate()
        val sourceJar = project.tasks.findByName("sourceJar")
        assertTrue("sourceJar is a Jar task", sourceJar is Jar)
    }
}
