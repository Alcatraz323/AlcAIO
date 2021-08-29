package io.alcatraz.alcaio.utils

import okio.*

import java.io.*

object IOUtils {
    fun okioRead(dir: String): String {
        var source: Source? = null
        var bufferedSource: BufferedSource? = null
        try {
            val file = File(dir)
            source = file.source()
            bufferedSource = source.buffer()
            return bufferedSource.readUtf8()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            closeQuietly(bufferedSource)
        }
        return "Failed to Read:$dir"
    }

    fun okioWrite(dir: String, content: String) {
        var sink: Sink? = null
        var bufferedSink: BufferedSink? = null
        try {
            val dest = File(dir)
            if (!dest.exists()) {
                try {
                    dest.parentFile.mkdirs()
                    dest.createNewFile()
                } catch (e: Exception) {
                }
            }
            sink = dest.sink()
            bufferedSink = sink.buffer()
            bufferedSink.writeUtf8(content)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            closeQuietly(bufferedSink)
        }
    }

    fun write(file: File, content: String) {
        if (!file.exists()) {
            try {
                file.parentFile.mkdirs()
                file.createNewFile()
            } catch (ignored : Exception) {
            }
        }
        file.writeText(content);
    }

    fun read(dir: String, rm: ReadMonitor?): String {
        var content = ""
        val file = File(dir)
        file.forEachLine {
            content += "\n"
            content += it
            rm?.onLine(it)
        }
        return content
    }

    fun renameFile(oldPath: String, newPath: String) {
        val oleFile = File(oldPath)
        val newFile = File(newPath)
        oleFile.renameTo(newFile)
    }

    fun delete(path: String): Boolean {
        val file = File(path)
        if (!file.exists()) {
            return false
        }
        if (file.isFile) {
            return file.delete()
        }
        val files = file.listFiles()
        for (f in files) {
            if (f.isFile) {
                if (!f.delete()) {
                    return false
                }
            } else {
                if (!delete(f.absolutePath)) {
                    return false
                }
            }
        }
        return file.delete()
    }

    interface ReadMonitor {
        fun onLine(line: String)

        fun callFinish()
    }

    private fun closeQuietly(closeable: Closeable?) {
        if (closeable != null) {
            try {
                closeable.close()
            } catch (rethrown: RuntimeException) {
                throw rethrown
            } catch (ignored: Exception) { }
        }
    }
}