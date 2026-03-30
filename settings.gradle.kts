rootProject.name = "KMPExam"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

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
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":resources")
include(":core")
include(":data")
include(":domain")
include(":designsystem")
include(":analytics")
include(":tracking")
include(":feature:onboarding")
include(":feature:home")
include(":feature:learn")
include(":feature:exam")
include(":feature:profile")
include(":feature:feedback")
include(":composeApp")