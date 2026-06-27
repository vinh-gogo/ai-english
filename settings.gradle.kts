pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.name = "ai-english"

include(":androidApp")
include(":composeApp")

// ── shared:core ──
include(":shared:core:common")
include(":shared:core:designsystem")
include(":shared:core:network")

// ── shared:feature:ai-teacher ──
include(":shared:feature:ai-teacher:domain")
include(":shared:feature:ai-teacher:application")
include(":shared:feature:ai-teacher:data")
include(":shared:feature:ai-teacher:ui")

// ── shared:feature:settings ── (add later if needed)
// include(":shared:feature:settings:domain")
// include(":shared:feature:settings:ui")
