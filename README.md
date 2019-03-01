
[![CircleCI](https://circleci.com/gh/kamatama41/gradle-git-release-plugin.svg?style=svg)](https://circleci.com/gh/kamatama41/gradle-git-release-plugin)

# gradle-plugin-git-release
A Gradle plugin that can release a Maven artifact into a Git repository, which is generally expected to be a [GitHub Pages](https://pages.github.com/).

## Supported Gradle version
It's tested on Gradle 5.2.1. It might not work on other version.

## Installation

```gradle
buildscript {
    repositories {
        jcenter()
        maven { url 'http://kamatama41.github.com/maven-repository/repository' }
    }
    dependencies {
        classpath("com.github.kamatama41:gradle-git-release-plugin:+")
    }
}

apply plugin: "com.github.kamatama41.git-release"
```

## Usage

Add `gitRelease` block into your `build.gradle`

```
gitRelease {
    groupId = "com.example"
    artifactId = "awesome-artifact"
    repoUri = "git@github.com:your_account/your_maven_repository.git"
}
```

then add `version` into your `gradle.properties`

```
version=0.1.0-SNAPSHOT
```

then run `gradle release`, so that:
- `version` will be updated to `0.1.0`
- New version's artifact will be published into a local maven repository temporarily
- The artifact will be committed and pushed to the remote Maven Git repository configured by `repoUri`
- New tag (`0.1.0`) will be added into the current Git repository
- `version` will be updated for the next version (`0.1.1-SNAPSHOT`)

## Parameters
- `groupId`: Your artifact's groupID (Required)
- `artifactId`: Your artifact's artifactId. If not set, this plugin tries to retrieve it from `rootProject.name`
- `repoUri`: URI for a Git repository used by Maven repository (Required)
- `repoBranch`: Branch name to be pushed. `gh-pages` is default.
- `repoPath`: Path that artifacts will be uploaded. `repository` is default.
- `repoDir`: Local path that artifacts will be published temporarily. `build/git-release` is default.
- `releaseBranch`: Branch name to be released. If current branch is not corresponding with the value, release will fail. `master` is default.

## Tips
This plugin uses [gradle-git-publish plugin](https://github.com/ajoberstar/gradle-git-publish) and [gradle-release plugin](https://github.com/researchgate/gradle-release), so you can use features of these plugins.
For example, if you want to skip confirmations of version increments, add `release.useAutomaticVersion=true` into build parameter.

```
gradle release -Prelease.useAutomaticVersion=true
```
