package pro.mezentsev.lympha.internal

import android.os.Handler
import android.os.Looper
import androidx.annotation.AnyThread
import pro.mezentsev.lympha.Event
import pro.mezentsev.lympha.Lympha

internal class LymphaInternal(builder: Builder): Lympha(builder) {
    private val handler = Handler(Looper.getMainLooper())

    @AnyThread
    internal fun inform(event: Event) {
        val isNotUiThread = Looper.myLooper() != Looper.getMainLooper()
        logger.d("[Thread(${event.threadName})] Obtained event message: '${event.message}' and took '${event.timeTaken} ms'")

        if (eventListeners.isEmpty()) {
            logger.d("Not event listeners defined")
        }

        for (listener in eventListeners) {
            val informAction = { listener.eventObtained(event) }

            if (isNotUiThread) {
                handler.post(informAction)
            } else {
                informAction()
            }
        }
    }
}