package pro.mezentsev.lympha.informer

import pro.mezentsev.lympha.events.Event
import pro.mezentsev.lympha.Lympha

object Informer {
    @JvmStatic
    fun inform(event: Event) {
        Lympha.instance.inform(event)
    }
}