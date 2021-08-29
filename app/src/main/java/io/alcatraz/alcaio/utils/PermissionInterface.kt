package io.alcatraz.alcaio.utils

interface PermissionInterface {
    fun onResult(requestCode: Int, permissions: Array<String>, granted: IntArray)
}
