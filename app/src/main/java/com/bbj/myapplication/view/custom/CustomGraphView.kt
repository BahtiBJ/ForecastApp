package com.bbj.myapplication.view.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.roundToInt
import kotlin.properties.Delegates

class CustomGraphView : View {

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    val xGuideline = Paint().apply {
        isAntiAlias = false
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }
    val yGuideline = Paint().apply {
        isAntiAlias = false
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    val pathPaint = Paint().apply {
        isAntiAlias = false
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    val textPaint = Paint().apply {
        isAntiAlias = false
        color = Color.WHITE
        textSize = 40f
        textAlign = Paint.Align.LEFT
    }

    val rectX = Rect()
    val rectY = Rect()

    val linePath = Path()

    var maxValueX: Float? = null
    var maxValueY: Float? = null

    var maxCoordinateX: Int? = 20
    var maxCoordinateY: Int? = 20
    var widthSize by Delegates.notNull<Int>()
    var heightSize by Delegates.notNull<Int>()
    var minCoordinateX: Int? = 400
    var minCoordinateY: Int? = 400

    private var listOfData = ArrayList<Coordinates>()
    private var listOfCoordinates = ArrayList<Coordinates>()


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        widthSize = MeasureSpec.getSize(widthMeasureSpec)
        heightSize = MeasureSpec.getSize(heightMeasureSpec)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (listOfData.isNotEmpty()) {
            drawLegend(canvas!!)
            drawLine(canvas)
        } else
            canvas?.drawText(
                "NO DATA",
                (maxCoordinateX?.div(2))!!.toFloat(),
                maxCoordinateY?.div(2)!!.toFloat(),
                textPaint
            )
    }

    fun setData(listOfData: ArrayList<Coordinates>) {
        requestLayout()
        Log.d("TAG", "Set Data 1 ${listOfData[0]}")
        this.listOfData = listOfData
        this.listOfCoordinates = listOfData

        maxValueX = listOfData.last().x
        maxValueY = listOfData.maxOf { it.y }



        listOfCoordinates.forEach {
            it.x = calculateX(it.x)
            it.y = calculateY(it.y)
        }

        invalidate()
    }

    private fun calculateX(realX: Float): Float {
        val cX = 80 + realX * (widthSize - 80) / maxValueX!!
        Log.d(
            "TAG",
            "maxCoord X $maxCoordinateX maxValueX $maxValueX Real X $realX Calculated X $cX"
        )
        return cX
    }

    private fun calculateY(realY: Float): Float {
        val cY = heightSize - ( 60 + realY * (heightSize - 60) / maxValueY!!)
        Log.d(
            "TAG",
            "maxCoord Y $maxCoordinateY maxValueY $maxValueY Real Y $realY Calculated Y $cY"
        )
        return cY
    }

    private fun drawLine(canvas: Canvas) {
        linePath.moveTo(listOfCoordinates[0].x, listOfCoordinates[0].y)
        listOfCoordinates.forEach {
            linePath.apply {
                lineTo(it.x, it.y)
            }
        }
        canvas.drawPath(linePath, pathPaint)
    }

    private fun drawLegend(canvas: Canvas) {
        var tempX = 0f
        var tempY = 0f
        for (index in 1..6) {
            tempX = maxValueX!! * index / 5
            tempY = maxValueY!! * (index - 1)/ 5
            canvas.drawText(
                tempX.roundToInt().toString(), calculateX(tempX) - 60f, heightSize.toFloat() , textPaint
            )
            canvas.drawText(
                tempY.roundToInt().toString(), 20f, calculateY(tempY) + 30f, textPaint
            )
        }
    }

}
