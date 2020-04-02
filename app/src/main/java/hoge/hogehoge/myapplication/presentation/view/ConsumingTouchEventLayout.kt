package hoge.hogehoge.myapplication.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout

class ConsumingTouchEventLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return true
    }
}
