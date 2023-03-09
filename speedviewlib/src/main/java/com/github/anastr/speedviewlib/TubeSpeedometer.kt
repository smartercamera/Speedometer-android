package com.github.anastr.speedviewlib

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.util.Log

/**
 * this Library build By Anas Altair
 * see it on [GitHub](https://github.com/anastr/SpeedView)
 */
open class TubeSpeedometer @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
) : Speedometer(context, attrs, defStyleAttr) {

    private val tubeOuterPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val tubePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val tubeBacPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val speedometerRect = RectF()


    var speedometerBackColor: Int
        get() = tubeBacPaint.color
        set(speedometerBackColor) {
            tubeBacPaint.color = speedometerBackColor
            invalidateGauge()
        }

    init {
        init()
        initAttributeSet(context, attrs)
    }

    override fun defaultGaugeValues() {
        super.speedometerWidth = dpTOpx(40f)
        sections[0].color = 0xff00BCD4.toInt()
        sections[1].color = 0xffFFC107.toInt()
        sections[2].color = 0xffF44336.toInt()
    }

    override fun defaultSpeedometerValues() {
        super.backgroundCircleColor = 0
    }

    private fun init() {
        tubePaint.style = Paint.Style.STROKE
        tubeBacPaint.style = Paint.Style.STROKE
        tubeBacPaint.color = 0xff5c90c8.toInt()
        tubeOuterPaint.style = Paint.Style.STROKE
        tubeOuterPaint.color = 0xffffffff.toInt()


        if (Build.VERSION.SDK_INT >= 11)
            setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    private fun initAttributeSet(context: Context, attrs: AttributeSet?) {
        if (attrs == null)
            return
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.TubeSpeedometer, 0, 0)

        tubeBacPaint.color = a.getColor(R.styleable.TubeSpeedometer_sv_speedometerBackColor, tubeBacPaint.color)
        a.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        super.onSizeChanged(w, h, oldW, oldH)

        updateBackgroundBitmap()
    }

    private fun initDraw() {
        tubePaint.strokeWidth = speedometerWidth
        tubePaint.color = Color.parseColor("#5c90c8")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        initDraw()

        val sweepAngle = (getEndDegree() - getStartDegree()) * getOffsetSpeed()
        canvas.drawArc(speedometerRect, getStartDegree().toFloat(), sweepAngle, false, tubePaint)

        drawSpeedUnitText(canvas)
        drawIndicator(canvas)
        drawNotes(canvas)
    }

    override fun updateBackgroundBitmap() {
        val c = createBackgroundBitmapCanvas()
        tubeBacPaint.strokeWidth = speedometerWidth

        val risk = speedometerWidth * .5f + padding
        val outerCircleArcRect = RectF()

        speedometerRect.set(risk + dpTOpx(6f), risk + dpTOpx(6f), size - risk - dpTOpx(6f), size - risk - dpTOpx(6f))

        outerCircleArcRect.set(risk, risk, size - risk, size - risk)


        c.drawArc(speedometerRect, getStartDegree().toFloat(), (getEndDegree() - getStartDegree()).toFloat(), false, tubeBacPaint)
        c.drawArc(outerCircleArcRect, getStartDegree().toFloat(), (getEndDegree() - getStartDegree()).toFloat(), false, tubeOuterPaint)

    }
}
