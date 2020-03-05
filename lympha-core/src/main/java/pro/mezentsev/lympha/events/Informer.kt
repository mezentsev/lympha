package pro.mezentsev.lympha.events

import pro.mezentsev.lympha.Lympha

object Informer {
    @JvmStatic
    fun inform(event: Event) {
        Lympha.instance.inform(event)
    }
}