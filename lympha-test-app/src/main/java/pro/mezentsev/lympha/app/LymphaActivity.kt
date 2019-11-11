package pro.mezentsev.lympha.app

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import pro.mezentsev.lympha.annotation.Lympha

class LymphaActivity : AppCompatActivity() {

    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lympha)
        textView = findViewById(R.id.text)

        textView.text = obtainNumber(5).toString()
        obtainThreadNumber(11)
    }

    @Lympha
    private fun obtainNumber(number: Int): Int {
        for(i in 0..1000000) {}
        Thread.sleep(50)
        return number * number
    }

    @Lympha
    private fun obtainThreadNumber(number: Int) {
        val uiThread = Handler(Looper.getMainLooper())
        val backgroundThread = HandlerThread("LymphaThread")
        backgroundThread.start()

        with(Handler(backgroundThread.looper)) {
            post {
                Thread.sleep(1000)
                val obtainedNumber = obtainNumber(number)
                uiThread.post { textView.text = obtainedNumber.toString() }
                backgroundThread.looper.quit()
            }
        }
    }
}
