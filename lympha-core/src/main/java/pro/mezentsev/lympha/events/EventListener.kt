package pro.mezentsev.lympha.events

import androidx.annotation.UiThread
import pro.mezentsev.lympha.events.Event

interface EventListener {
    @UiThread
    fun eventObtained(event: Event)
}