package ir.roocket.sinadalvand.todo.view.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import ir.roocket.sinadalvand.todo.R
import ir.roocket.sinadalvand.todo.utils.Extension.dpPx

class SubtaskIndicator @JvmOverloads constructor(
    context: Context, val attrs: AttributeSet? = null, val defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    /* number of dot */
    private var count = 4

    /* number of that should fill */
    private var filledCount = 3

    /* dot radius */
    private var radius = 2.dpPx.toFloat()

    /* dot gaps */
    private var gap = 6.dpPx.toFloat()

    /* filled dot color */
    private var filledColor = Color.RED

    /* empty dot color */
    private var emptyColor = Color.WHITE

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        getAttrs()
    }

    private fun getAttrs() {
        val a = context.obtainStyledAttributes(attrs, R.styleable.SubtaskIndicator)
        filledColor = a.getColor(R.styleable.SubtaskIndicator_dot_fill_color, filledColor)
        emptyColor = a.getColor(R.styleable.SubtaskIndicator_dot_empty_color, emptyColor)
        radius = a.getDimension(R.styleable.SubtaskIndicator_dot_radius, radius)
        gap = a.getDimension(R.styleable.SubtaskIndicator_dot_gap, gap)
        a.recycle()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val width = width / 2
        val height = height

        var rawHeight = (height / 2f) - ((((count - 1) * gap) / 2) + ((count - 1) * radius))

        for (i in 1..count) {
            canvas?.drawCircle(
                width / 1f,
                rawHeight,
                radius.toFloat(),
                paint.apply { color = if (i <= filledCount) filledColor else emptyColor }
            )
            rawHeight += ((radius * 2) + gap)
        }
    }

    fun setIndicateCounts(counts: Int) {
        this.count = counts
        invalidate()
    }

    fun setIndicateFillsCount(counts: Int) {
        this.filledCount = counts
        invalidate()
    }


}