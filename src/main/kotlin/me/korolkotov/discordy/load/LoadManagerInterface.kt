package me.korolkotov.discordy.load

interface LoadManagerInterface<T> {
    fun getInstance(): T

    fun initialize()
    fun terminate() {}
    fun reload() {}
}