package com.github.kamatama41.gradle.gitrelease

import org.eclipse.jgit.internal.storage.file.FileRepository
import org.eclipse.jgit.junit.RepositoryTestCase
import org.eclipse.jgit.transport.RemoteConfig
import org.eclipse.jgit.transport.URIish
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class GitReleasePluginTest : RepositoryTestCase() {
    lateinit var testProjectDir: FileRepository
    lateinit var mavenRepo: RemoteConfig
    val projectDir by lazy { testProjectDir.directory!! }
    val buildDir by lazy { "${projectDir.absolutePath}/build/git-release-test" }

    override fun setUp() {
        super.setUp()
        setupRemote()
        setupProject()
    }

    // TODO: test 'release' task

    @Test
    fun testPublish() {
        val result = build("publish").task(":publish")
        assertThat(result.outcome, `is`(TaskOutcome.SUCCESS))
        assertTrue(File("$buildDir/test-repository/com/example/awesome-artifact/0.1.0-SNAPSHOT").exists())
    }

    private fun setupProject() {
        val repo = createWorkRepository()
        testProjectDir = repo

        createFile("build.gradle").writeText("""
            plugins { id 'com.github.kamatama41.git-release' }

            gitRelease {
                groupId = 'com.example'
                artifactId = 'awesome-artifact'
                repoUri = '${mavenRepo.urIs[0]}'
                repoDir = file('$buildDir')
                repoPath = 'test-repository'
            }
        """)
        createFile("gradle.properties").writeText("version=0.1.0-SNAPSHOT")
    }

    private fun setupRemote() {
        val repo = createBareRepository()
        val config = repo.config
        val remoteConfig = RemoteConfig(config, "test")
        val uri = URIish(repo.directory.toURI().toURL())
        remoteConfig.addURI(uri)
        remoteConfig.update(config)
        config.save()
        mavenRepo = remoteConfig
    }

    private fun build(vararg args: String): BuildResult = newRunner(*args, "--stacktrace").build()

    private fun newRunner(vararg args: String): GradleRunner = GradleRunner.create()
            .withProjectDir(projectDir)
            .withArguments(*args)
            .withPluginClasspath()

    private fun createFile(name: String) = File(projectDir, name)
}
