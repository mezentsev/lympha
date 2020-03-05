package pro.mezentsev.lympha

import pro.mezentsev.lympha.events.EventListener
import pro.mezentsev.lympha.internal.Core
import pro.mezentsev.lympha.logger.Logger

object Lympha {
    private lateinit var builder: Builder

    internal val instance by lazy {
        if (!::builder.isInitialized) {
            throw IllegalStateException("You must call init(...) fist")
        }

        Core(builder)
    }

    @JvmStatic
    @JvmOverloads
    fun init(builder: Builder = Builder()) {
        this.builder = builder
    }

    @JvmStatic
    fun addEventListener(eventListener: EventListener): Boolean =
        instance.addEventListener(eventListener)

    @JvmStatic
    fun removeEventListener(eventListener: EventListener): Boolean =
        instance.removeEventListener(eventListener)

    class Builder @JvmOverloads constructor(val logger: Logger = Logger.Simple())
}