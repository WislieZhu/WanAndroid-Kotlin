package com.wislie.common.util

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat

object CameraUtil {


    fun takeCamera(context: Context, block:(String)->Unit): Intent {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(context.packageManager) != null) {
            val imageFileName =
                "PNG${SimpleDateFormat("yyyyMMdd_HHmmss").format(System.currentTimeMillis())}.png"
            val imageFile = File(
                context.getExternalFilesDir("${Environment.DIRECTORY_PICTURES}/photo"),
                imageFileName
            )
            val imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(
                    context,
                    "${context.applicationInfo.processName}.FileProvider",
                    imageFile
                )
            } else {
                Uri.fromFile(imageFile)
            }
            block.invoke(imageFile.path)
            Log.i("wislieZhu","imageUri---$imageUri")
            grantPermission(context, imageUri, intent)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0)
        }
        return intent
    }



    private fun grantPermission(context: Context, fileUri: Uri, intent: Intent) {
        val resInfoList = context.packageManager
            .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            context.grantUriPermission(
                packageName, fileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                        or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
        }
    }

    fun selectFromAlbum():Intent {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        return intent
    }

    fun getPath2uri(context: Context, fileUri: Uri): String? {
        if (DocumentsContract.isDocumentUri(context, fileUri)) {
            if (isExternalStorageDocument(fileUri)) {
                val docId = DocumentsContract.getDocumentId(fileUri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return context.getExternalFilesDir(null).toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(fileUri)) {
                val id = DocumentsContract.getDocumentId(fileUri)
                if (id.startsWith("raw:")) {
                    return id.replaceFirst("raw:".toRegex(), "")
                }
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(fileUri)) {
                val docId = DocumentsContract.getDocumentId(fileUri)
                val split = docId.split(":").toTypedArray()
                val contentUri: Uri = when (split[0]) {
                    "image" -> {
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    }
                    "video" -> {
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    }
                    "audio" -> {
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    else -> {
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                }
                val selection = MediaStore.Images.Media._ID + "=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } // MediaStore (and general)
        else if ("content".equals(fileUri.scheme, ignoreCase = true)) {
            // Return the remote address
            return if (isGooglePhotosUri(fileUri)) {
                fileUri.lastPathSegment
            } else getDataColumn(context, fileUri, null, null)
        } else if ("file".equals(fileUri.scheme, ignoreCase = true)) {
            return fileUri.path
        }
        return null
    }

    private fun getDataColumn(
        context: Context,
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow(column))
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }
}