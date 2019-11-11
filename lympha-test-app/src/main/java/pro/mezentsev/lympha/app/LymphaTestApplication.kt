package pro.mezentsev.lympha.app

import android.app.Application
import pro.mezentsev.lympha.Lympha

class LymphaTestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Lympha.init()
    }
}