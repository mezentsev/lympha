package pro.mezentsev.lympha

import pro.mezentsev.lympha.internal.LymphaInternal

open class Lympha(private val builder: Builder) {
    protected val logger = builder.logger

    protected val eventListeners = mutableListOf<EventListener>()

    companion object {
        @Volatile
        private var instance: Lympha? = null

        internal fun getInstance() = instance
            ?: throw IllegalStateException("Not init yet")

        fun init(builder: Builder = Builder()) {
            if (instance != null) {
                throw IllegalStateException("Already init")
            }

            synchronized(this) {
                if (instance == null) {
                    instance = LymphaInternal(builder)
                } else {
                    throw IllegalStateException("Already init")
                }
            }
        }

        fun addEventListener(eventListener: EventListener): Boolean =
            getInstance().eventListeners.add(eventListener)

        fun removeEventListener(eventListener: EventListener): Boolean =
            getInstance().eventListeners.remove(eventListener)
    }

    class Builder(val logger: Logger = Logger.Simple())
}