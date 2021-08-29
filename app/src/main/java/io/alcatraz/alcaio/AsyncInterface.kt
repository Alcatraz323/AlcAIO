package io.alcatraz.alcaio

interface AsyncInterface<T> {
    fun onDone(result: T)
}
