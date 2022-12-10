package com.example.dcom.presentation.common

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import com.example.dcom.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun Context.resIdByName(resIdName: String?, resType: String): Int {
    resIdName?.let {
        return resources.getIdentifier(it, resType, packageName)
    }
    throw Resources.NotFoundException()
}

fun getStringByIdName(context: Context, idName: String?): String {
    val res = context.resources
    return res.getString(res.getIdentifier(idName, "string", context.packageName))
}

const val THEME_KEY = "suck"

const val THEME_TOAST_KEY = "theme_toast_key"

fun deleteCache(context: Context) {
    try {
        val dir: File = context.cacheDir
        deleteDir(dir)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun deleteDir(dir: File?): Boolean {
    return if (dir != null && dir.isDirectory) {
        val children: Array<String> = dir.list() as Array<String>
        for (i in children.indices) {
            val success = deleteDir(File(dir, children[i]))
            if (!success) {
                return false
            }
        }
        dir.delete()
    } else if (dir != null && dir.isFile) {
        dir.delete()
    } else {
        false
    }
}

fun Activity.emptyDatabase() {
    // create a scope to access the database from a thread other than the main thread
    val scope = CoroutineScope(Dispatchers.Default)
    scope.launch {
        AppDatabase.getInstance(this@emptyDatabase).clearAllTables()
    }
}

// convert time long to hh:mm dd/MM/yyyy
fun convertTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("hh:mm dd/MM/yyyy", Locale.getDefault())
    return format.format(date)
}

