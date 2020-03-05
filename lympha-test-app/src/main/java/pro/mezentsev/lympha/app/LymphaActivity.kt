package pro.mezentsev.lympha.app

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import obtainNumber
import obtainThreadNumber
import pro.mezentsev.lympha.events.Event
import pro.mezentsev.lympha.events.EventListener
import pro.mezentsev.lympha.Lympha
import pro.mezentsev.lympha.annotation.LymphaProfiler
import kotlin.random.Random

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

    }

    @LymphaProfiler
    fun obtain() {
        obtainNumber(5)
        obtainThreadNumber(11)
    }

    override fun onResume() {
        super.onResume()
        Lympha.addEventListener(eventListener)
        obtain()
    }
    override fun onPause() {
        super.onPause()
        Lympha.removeEventListener(eventListener)
    }
}
