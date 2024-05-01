
pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage") /* TODO remove when stable */
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "Counters_2"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":app-rn3-catalog")
include(":benchmarks")
include(":core:analytics")
include(":core:config")
include(":core:common")
include(":core:data")
include(":core:data-test")
include(":core:database")
include(":core:datastore")
include(":core:datastore-proto")
include(":core:datastore-test")
include(":core:designsystem")
include(":core:domain")
include(":core:model")
include(":core:screenshot-testing")
include(":core:testing")
include(":core:ui")
include(":core:feedback")
include(":core:accessibility")

include(":feature:settings")
include(":feature:aboutme")

include(":lint")
include(":ui-test-hilt-manifest")
