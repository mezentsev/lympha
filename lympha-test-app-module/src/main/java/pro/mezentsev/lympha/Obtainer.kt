import android.os.Handler
import android.os.HandlerThread
import pro.mezentsev.lympha.annotation.LymphaProfiler

@LymphaProfiler
fun obtainNumber(number: Int): Int {
    Thread.sleep(50)
    return number * number
}

@LymphaProfiler
fun obtainThreadNumber(number: Int) {
    val backgroundThread = HandlerThread("LymphaThread")
    backgroundThread.start()

    with(Handler(backgroundThread.looper)) {
        post {
            Thread.sleep(1000)
            obtainNumber(number)
            backgroundThread.looper.quit()
        }
    }
}