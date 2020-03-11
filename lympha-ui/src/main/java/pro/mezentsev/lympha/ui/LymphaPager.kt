package pro.mezentsev.lympha.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2

class LymphaPager
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {

    val viewPager = ViewPager2(context, attrs, defStyleAttr)

}