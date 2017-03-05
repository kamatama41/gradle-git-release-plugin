package com.github.kamatama41.gradle.gitrelease

import org.gradle.api.Project

inline fun <reified T : Any> Project.the(): T =
        convention.findPlugin(T::class.java) ?: convention.getByType(T::class.java)!!
