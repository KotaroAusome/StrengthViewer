package com.example.strengthviewer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Point
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

open class NodeBase(context: Context) : AppCompatButton(context) {
    private lateinit var _positionXY: Point
    private lateinit var _connectPosXY: Point

    var positionXY: Point
        get() = _positionXY
        set(value) {
            _positionXY = value
            _connectPosXY = Point(value.x, value.y + CommonFunction.heightNode / 2)
        }

    var connectPosXY: Point
        get() = _connectPosXY
        set(value) {
            _connectPosXY = value
        }

    fun init(attrs: AttributeSet?, defStyle: Int) {
        setBackgroundResource(R.drawable.shape_sample)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }
}