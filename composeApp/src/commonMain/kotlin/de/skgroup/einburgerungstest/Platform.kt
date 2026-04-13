package de.skgroup.einburgerungstest

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform