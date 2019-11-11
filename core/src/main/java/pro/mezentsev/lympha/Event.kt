package pro.mezentsev.lympha

abstract class Event {
    abstract val message: String
    open val timeTaken: Long = 0L
    open val timestamp: Long = System.currentTimeMillis()
    open val threadName = Thread.currentThread().name

    open class Simple constructor(override val message: String) : Event()
}