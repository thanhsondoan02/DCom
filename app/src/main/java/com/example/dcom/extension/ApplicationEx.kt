//package ai.ftech.base.extension
//
//import ai.ftech.base.common.BasePreference
//import android.app.Application
//
//private var application: Application? = null
//
//fun setApplication(context: Application) {
//    application = context
//}
//
//fun getApplication() = application ?: throw RuntimeException("Application context mustn't null")
//
//private var basePreference: BasePreference? = null
//
//fun Application.initPrefData(preferenceName: String) {
//    basePreference = BasePreference(preferenceName, this)
//}
//
//fun <T> Class<T>.getPrefData(key: String, defaultValue: T): T =
//    basePreference!!.get(key, defaultValue, this)
//
//fun <T> putPrefData(key: String, value: T) = basePreference!!.put(key, value)
//
//const val BOOLEAN_DEFAULT = false
//const val INT_DEFAULT = 0
//const val LONG_DEFAULT = 0L
//const val DOUBLE_DEFAULT = 0.0
//const val FLOAT_DEFAULT = 0f
//const val STRING_DEFAULT = ""
//
