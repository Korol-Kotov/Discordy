package me.korolkotov.discordy.util

import java.time.Clock

object TimeUtil {
    fun now() = Clock.systemUTC().instant()!!
}