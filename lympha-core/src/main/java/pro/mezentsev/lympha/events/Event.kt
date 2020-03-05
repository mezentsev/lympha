package pro.mezentsev.lympha.events

open class Event @JvmOverloads constructor(
    val channel: Class<*>,
    val message: String,
    val timeTaken: Long = 0L,
    val timestamp: Long = System.currentTimeMillis(),
    val threadName: String = Thread.currentThread().name
)