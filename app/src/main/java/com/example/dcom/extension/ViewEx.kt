package com.example.dcom.extension

import android.app.Activity
import android.graphics.Insets
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowInsets
import android.widget.TextView

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

fun TextView.setImageLeft(drawable: Drawable?) {
    setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
}

fun TextView.setImageTop(drawable: Drawable?) {
    setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
}

fun TextView.setImageRight(drawable: Drawable?) {
    setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
}

fun TextView.setImageBottom(drawable: Drawable?) {
    setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable)
}

fun TextView.clearImage() {
    setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
}

//fun getAppString(
//    @StringRes stringId: Int,
//    context: Context? = getApplication()
//): String {
//    return context?.getString(stringId) ?: ""
//}
//
//fun getAppString(
//    @StringRes stringId: Int,
//    vararg params: Any,
//    context: Context? = getApplication()
//): String {
//    return context?.getString(stringId, *params) ?: ""
//}
//
//fun getAppSpannableString(
//    @StringRes stringId: Int,
//    context: Context? = getApplication()
//): SpannableString {
//    return SpannableString(context?.getString(stringId))
//}
//
//fun getAppFont(
//    @FontRes fontId: Int,
//    context: Context? = getApplication()
//): Typeface? {
//    return context?.let {
//        ResourcesCompat.getFont(it, fontId)
//    }
//}
//
//fun getAppDrawable(
//    @DrawableRes drawableId: Int,
//    context: Context? = getApplication()
//): Drawable? {
//    return context?.let {
//        ContextCompat.getDrawable(it, drawableId)
//    }
//}
//
//fun getAppDimensionPixel(
//    @DimenRes dimenId: Int,
//    context: Context? = getApplication()
//): Int {
//    return context?.resources?.getDimensionPixelSize(dimenId) ?: -1
//}
//
//fun getAppDimension(
//    @DimenRes dimenId: Int,
//    context: Context? = getApplication()
//): Float {
//    return context?.resources?.getDimension(dimenId) ?: -1f
//}
//
//fun getAppColor(
//    @ColorRes colorRes: Int,
//    context: Context? = getApplication()
//): Int {
//    return context?.let {
//        ContextCompat.getColor(it, colorRes)
//    } ?: Color.TRANSPARENT
//}

fun Activity.getScreenHeight(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = windowManager.currentWindowMetrics
        val insets: Insets = windowMetrics.windowInsets
            .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        windowMetrics.bounds.height() - insets.top - insets.bottom
    } else {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        displayMetrics.heightPixels
    }
}

//fun getAppAnim(
//    @AnimRes animRes: Int,
//    context: Context? = getApplication()
//): Int {
//    return context?.resources.getAnimation(animRes) ?: -1
//}


