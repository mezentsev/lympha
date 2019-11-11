package pro.mezentsev.lympha.internal

import pro.mezentsev.lympha.Event
import pro.mezentsev.lympha.Lympha

internal object LymphaInformer {
    private val instance: LymphaInternal by lazy { Lympha.getInstance() as LymphaInternal }

    @JvmStatic
    fun inform(event: Event) {
        instance.inform(event)
    }
}