package pro.mezentsev.lympha.app

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import pro.mezentsev.lympha.Event
import pro.mezentsev.lympha.EventListener
import pro.mezentsev.lympha.annotation.Lympha
import kotlin.random.Random
import pro.mezentsev.lympha.Lympha as LymphaCore

class LymphaActivity : AppCompatActivity() {

    lateinit var fireButton: Button
    lateinit var textView: TextView

    private val eventListener = object : EventListener {
        override fun eventObtained(event: Event) {
            textView.text = "${textView.text}[${event.threadName}] ${event.message}\n"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lympha)
        textView = findViewById(R.id.text)
        fireButton = findViewById(R.id.fire)

        fireButton.setOnClickListener {
            obtainNumber(Random.nextInt(100))
            obtainThreadNumber(Random.nextInt(100))
        }

        obtainNumber(5)
        obtainThreadNumber(11)
    }

    @Lympha
    private fun obtainNumber(number: Int): Int {
        Thread.sleep(50)
        return number * number
    }

    @Lympha
    private fun obtainThreadNumber(number: Int) {
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

    override fun onResume() {
        super.onResume()
        LymphaCore.addEventListener(eventListener)
    }
    override fun onPause() {
        super.onPause()
        LymphaCore.removeEventListener(eventListener)
    }
}
