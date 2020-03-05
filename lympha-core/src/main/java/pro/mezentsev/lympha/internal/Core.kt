package pro.mezentsev.lympha.internal

import android.os.Handler
import android.os.Looper
import androidx.annotation.AnyThread
import pro.mezentsev.lympha.events.Event
import pro.mezentsev.lympha.events.EventListener
import pro.mezentsev.lympha.Lympha
import java.util.concurrent.CopyOnWriteArraySet

internal class Core(private val builder: Lympha.Builder) {
    private val logger = builder.logger
    private val eventListeners = CopyOnWriteArraySet<EventListener>()

    private val handler = Handler(Looper.getMainLooper())

    /**
     * Informs on UIThread.
     */
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

    internal fun addEventListener(eventListener: EventListener): Boolean {
        return eventListeners.add(eventListener)
    }

    internal fun removeEventListener(eventListener: EventListener): Boolean {
        return eventListeners.remove(eventListener)
    }
}