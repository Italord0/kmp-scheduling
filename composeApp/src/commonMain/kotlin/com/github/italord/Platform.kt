package com.github.italord

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform