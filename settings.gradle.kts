
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
rootProject.name = "ReBased"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")

include(":core:accessibility")
include(":core:analytics")
include(":core:auth")
include(":core:common")
include(":core:config")
include(":core:data")
include(":core:data-test")
include(":core:datastore")
include(":core:datastore-proto")
include(":core:datastore-test")
include(":core:designsystem")
include(":core:feedback")
include(":core:model")
include(":core:screenshot-testing")
include(":core:shapes")
include(":core:testing")
include(":core:ui")

include(":feature:localfeed")
include(":feature:login")
include(":feature:settings")

include(":lint")
include(":ui-test-hilt-manifest")
