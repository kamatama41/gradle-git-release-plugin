package com.github.kamatama41.gradle.gitrelease

import net.researchgate.release.GitAdapter
import net.researchgate.release.ReleaseExtension
import net.researchgate.release.ReleasePlugin
import org.ajoberstar.gradle.git.publish.GitPublishExtension
import org.ajoberstar.gradle.git.publish.GitPublishPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.tasks.bundling.Jar

class GitReleasePlugin : Plugin<Project> {
    companion object {
        val extensionName = "gitRelease"
    }

    override fun apply(project: Project) {
        project.extensions.create(extensionName, GitReleaseExtension::class.java, project)

        project.plugins.apply(JavaPlugin::class.java)
        project.plugins.apply(MavenPublishPlugin::class.java)
        project.plugins.apply(GitPublishPlugin::class.java)
        project.plugins.apply(ReleasePlugin::class.java)

        project.afterEvaluate { pj ->
            configure(pj)
        }
    }

    fun configure(project: Project) {
        val extension = project.extensions.findByName(extensionName) as GitReleaseExtension

        ///////////////////////////////////////////////
        // Task configurations
        ///////////////////////////////////////////////

        // maven-publish
        val publish = project.tasks.findByName("publish")
        // git-publish
        val gitPublishReset = project.tasks.findByName("gitPublishReset")
        val gitPublishCopy = project.tasks.findByName("gitPublishCopy")
        val gitPublishCommit = project.tasks.findByName("gitPublishCommit")
        val gitPublishPush = project.tasks.findByName("gitPublishPush")
        // release
        val afterReleaseBuild = project.tasks.findByName("afterReleaseBuild")

        publish.dependsOn(gitPublishReset)
        gitPublishCopy.enabled = false
        gitPublishCommit.dependsOn(publish)
        afterReleaseBuild.dependsOn(gitPublishPush)

        val sourceJar = sourceJarTask(project)

        ///////////////////////////////////////////////
        // Extension configurations
        ///////////////////////////////////////////////

        // Git publish
        project.extensions.configure(GitPublishExtension::class.java) { gitPublish ->
            gitPublish.repoUri = extension.repoUri
            gitPublish.branch = extension.repoBranch
            gitPublish.repoDir = extension.repoDir

            gitPublish.preserve { it.include("**") } // All files will be kept
        }

        // Maven publish
        project.extensions.configure(PublishingExtension::class.java) { publishing ->
            publishing.publications { container ->
                container.create("maven", MavenPublication::class.java) { maven ->
                    maven.groupId = extension.groupId
                    maven.artifactId = extension.artifactId

                    val java = project.components.findByName("java")
                    maven.from(java)

                    // Add source jar to publication
                    maven.artifact(sourceJar)
                }
            }

            publishing.repositories { repository ->
                repository.maven { maven ->
                    maven.setUrl("file://${project.file("${extension.repoDir}/${extension.repoPath}").absolutePath}")
                }
            }
        }

        // Release
        project.extensions.configure(ReleaseExtension::class.java) { release ->
            @Suppress("UNCHECKED_CAST")
            val config = GitAdapter(project, release.getProperty("attributes") as Map<String, Any>)
                    .createNewConfig() as GitAdapter.GitConfig
            config.requireBranch = extension.releaseBranch

            release.setProperty("git", config)
        }
    }

    fun sourceJarTask(project: Project): Task {
        val sourceSets = project.the<JavaPluginConvention>().sourceSets
        return project.tasks.create("sourceJar", Jar::class.java) { task ->
            task.group = "build"

            val main = sourceSets.findByName("main")
            task.from(main.allSource)
            task.classifier = "sources"
        }
    }
}
