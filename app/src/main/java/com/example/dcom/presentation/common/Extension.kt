package com.example.dcom.presentation.common

import android.content.Context
import android.content.res.Resources

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
