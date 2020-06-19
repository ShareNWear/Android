package com.myoutfit.utils.extentions

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

fun Context?.getPath(uri: Uri?): String? {

    // DocumentProvider
    if (this == null || uri == null) return null
    if (DocumentsContract.isDocumentUri(this, uri)) {
        // ExternalStorageProvider
        if (isExternalStorageDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]

            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }

        } else if (isDownloadsDocument(uri)) {

            val id = DocumentsContract.getDocumentId(uri)
            val contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
            )

            return getDataColumn(this, contentUri, null, null)
        } else if (isMediaDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]

            var contentUri: Uri? = null
            when (type) {
                "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }

            val selection = "_id=?"
            val selectionArgs = arrayOf(split[1])

            return getDataColumn(this, contentUri, selection, selectionArgs)
        }// MediaProvider
        // DownloadsProvider
    } else if ("content".equals(uri.scheme, ignoreCase = true)) {
        return getDataColumn(this, uri, null, null)
    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
        return uri.path
    }

    // File
    // MediaStore (and general)

    return null
}

/**
 * Get the value of the data column for this Uri. This is useful for
 * MediaStore Uris, and other file-based ContentProviders.
 *
 * @param context The context.
 * @param uri The Uri to query.
 * @param selection (Optional) Filter used in the query.
 * @param selectionArgs (Optional) Selection arguments used in the query.
 * @return The value of the _data column, which is typically a file path.
 */
private fun getDataColumn(
    context: Context, uri: Uri?, selection: String?,
    selectionArgs: Array<String>?
): String? {

    var cursor: Cursor? = null
    val column = MediaStore.Images.Media.DATA
    val projection = arrayOf(column)

    try {
        cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            return cursor.getString(columnIndex)
        }
    } finally {
        cursor?.close()
    }
    return null
}


/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is ExternalStorageProvider.
 */
private fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 */
private fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 */
private fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}

fun Context.createPartFromUri(partName: String, parse: String, uri: Uri): MultipartBody.Part? {
    val file = File(this.getPath(uri))
    val mRequestBody = RequestBody.create(MediaType.parse(parse), file)
    return MultipartBody.Part.createFormData(partName, file.name, mRequestBody)
}

fun createPartFromStringUri(partName: String, parse: String, uri: String): MultipartBody.Part? {
    val file = File(uri)
    val mRequestBody = RequestBody.create(MediaType.parse(parse), file)
    return MultipartBody.Part.createFormData(partName, file.name, mRequestBody)
}

fun createPartFromStringUri(partName: String, parse: String, file: File?): MultipartBody.Part? {
    return file?.let {
        val mRequestBody = RequestBody.create(MediaType.parse(parse), file)
        return MultipartBody.Part.createFormData(partName, file.name, mRequestBody)
    }
}
