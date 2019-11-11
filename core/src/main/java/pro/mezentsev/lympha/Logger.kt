package pro.mezentsev.lympha

import android.util.Log
import java.util.logging.Logger

private const val TAG = "LYMPHA"

interface Logger {
    fun d(message: String?)
    fun v(message: String?)
    fun w(throwable: Throwable, message: String?)
    fun e(throwable: Throwable, message: String?)

    open class Simple : pro.mezentsev.lympha.Logger {
        override fun d(message: String?) {
            Log.d(TAG, message)
        }

        override fun v(message: String?) {
            Log.v(TAG, message)
        }

        override fun w(throwable: Throwable, message: String?) {
            Log.v(TAG, message, throwable)
        }

        override fun e(throwable: Throwable, message: String?) {
            Log.e(TAG, message, throwable)
        }

    }
}