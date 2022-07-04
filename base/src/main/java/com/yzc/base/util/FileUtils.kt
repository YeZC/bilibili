package com.yzc.base.util

import java.io.File
import java.io.IOException

object FileUtils {

    fun deleteDirectory(directory: File?): Boolean {
        if (directory == null || !directory.exists() || !directory.isDirectory) return false
        if (directory.exists() && directory.isDirectory) {
            for (listFile in directory.listFiles()) {
                if (listFile.isFile) {
                    listFile.delete()
                } else if (listFile.isDirectory) {
                    deleteDirectory(listFile)
                }
            }
        }
        return true
    }

}