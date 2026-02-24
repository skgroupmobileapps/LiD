package de.skabs.skgroup

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform