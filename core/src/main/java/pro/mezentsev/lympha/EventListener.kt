package pro.mezentsev.lympha

import androidx.annotation.UiThread
import kotlin.reflect.KClass

interface EventListener {
    @UiThread
    fun eventObtained(event: Event)
}