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
include(":shared:core:database")
include(":shared:core:designsystem")
include(":shared:core:network")

// ── shared:feature:ai-teacher ──
include(":shared:feature:ai-teacher:domain")
include(":shared:feature:ai-teacher:application")
include(":shared:feature:ai-teacher:data")
include(":shared:feature:ai-teacher:ui")

// ── shared:feature:profile ──
include(":shared:feature:profile:domain")
include(":shared:feature:profile:application")
include(":shared:feature:profile:data")
include(":shared:feature:profile:ui")

// ── shared:feature:history ──
include(":shared:feature:history:domain")
include(":shared:feature:history:application")
include(":shared:feature:history:data")
include(":shared:feature:history:ui")

// ── shared:feature:pro ──
include(":shared:feature:pro:domain")
include(":shared:feature:pro:application")
include(":shared:feature:pro:data")
include(":shared:feature:pro:ui")

// ── shared:feature:grammar-graph ──
include(":shared:feature:grammar-graph:domain")
include(":shared:feature:grammar-graph:application")
include(":shared:feature:grammar-graph:data")
include(":shared:feature:grammar-graph:ui")

// ── shared:feature:settings ── (add later if needed)
// include(":shared:feature:settings:domain")
// include(":shared:feature:settings:ui")
