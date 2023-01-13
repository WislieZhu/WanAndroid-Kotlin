package com.wislie.wanandroid.util

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.File
import java.nio.file.Files

object SaveUtil {

    fun saveImgToAlbum(context: Context, imageFile: String): Boolean {
        return try {
            val localContentResolver = context.contentResolver
            val tempFile = File(imageFile)
            val localContentValues = getImageContentValues(tempFile, System.currentTimeMillis())
            val uri = localContentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                localContentValues
            )
            copyFileAfterQ(context, localContentResolver, tempFile, uri)
            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun  getImageContentValues(paramFile:File, timestamp:Long): ContentValues {
        val localContentValues =  ContentValues()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            localContentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/Camera")
        }
        localContentValues.put(MediaStore.Images.Media.TITLE, paramFile.name)
        localContentValues.put(MediaStore.Images.Media.DISPLAY_NAME, paramFile.name)
        localContentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        localContentValues.put(MediaStore.Images.Media.DATE_TAKEN, timestamp)
        localContentValues.put(MediaStore.Images.Media.DATE_MODIFIED, timestamp)
        localContentValues.put(MediaStore.Images.Media.DATE_ADDED, timestamp)
        localContentValues.put(MediaStore.Images.Media.ORIENTATION, 0)
        localContentValues.put(MediaStore.Images.Media.DATA, paramFile.absolutePath)
        localContentValues.put(MediaStore.Images.Media.SIZE, paramFile.length())
        return localContentValues
    }

    private fun copyFileAfterQ(context:Context, localContentResolver: ContentResolver,  tempFile:File,  localUri: Uri?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
            context.applicationInfo.targetSdkVersion >= Build.VERSION_CODES.R) {
            //拷贝文件到相册的uri,android11及以上得这么干，否则不会显示。可以参考ScreenMediaRecorder的save方法
            val os = localUri?.let { localContentResolver.openOutputStream(it, "w") }
            os.use {
                Files.copy(tempFile.toPath(), os)
                tempFile.deleteOnExit()
            }
        }
    }
   

}