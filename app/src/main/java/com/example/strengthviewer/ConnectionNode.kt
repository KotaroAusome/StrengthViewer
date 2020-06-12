package com.example.strengthviewer

import android.content.Context
import android.graphics.*
import android.view.View

class ConnectionNode : View {
    private lateinit var positionXYStart: Point
    private lateinit var positionXYEnd: Point

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, xy_start: Point, xy_end: Point) : super(context) {
        positionXYStart = xy_start
        positionXYEnd = xy_end
    }

    override fun onDraw(canvas: Canvas) {
        val paint_s = Paint()
        val paint_e = Paint()
        //直径を50に設定
        paint_s.strokeWidth = 30.0F
        //形を正方形に設定
        paint_s.strokeCap = Paint.Cap.ROUND
        //色を赤色に設定
        paint_s.color = Color.RED
        //x:100,y:100の位置に点を描画
        canvas.drawPoint(positionXYStart.x.toFloat(), positionXYStart.y.toFloat(), paint_s)

        //直径を50に設定
        paint_e.strokeWidth = 30.0F
        //形を正方形に設定
        paint_e.strokeCap = Paint.Cap.ROUND
        //色を赤色に設定
        paint_e.color = Color.BLUE
        //x:100,y:100の位置に点を描画
        canvas.drawPoint(positionXYEnd.x.toFloat(), positionXYEnd.y.toFloat(), paint_e)
        canvas.drawPoint(
            (3 * (positionXYEnd.x - positionXYStart.x) / 5).toFloat(),
            (positionXYStart.y + (1 * (positionXYEnd.y - positionXYStart.y) / 5)).toFloat(),
            paint_e
        )

        //この間にグラフィック描画のコードを記述する
        val paint_sen = Paint()
        //色を設定
        paint_sen.color = Color.GREEN
        //線の太さを指定
        paint_sen.strokeWidth = 5f
        //線を滑らかに書く
        paint_sen.isAntiAlias = true
        // 線で描く
        paint_sen.style = Paint.Style.STROKE
        //pathを新規作成
        val path = Path()
        //始点座標
        path.moveTo(positionXYStart.x.toFloat(), positionXYStart.y.toFloat())
        path.cubicTo(
            (3 * (positionXYEnd.x - positionXYStart.x) / 5).toFloat(),
            (positionXYStart.y + (1 * (positionXYEnd.y - positionXYStart.y) / 5)).toFloat(),
            (5 * (positionXYEnd.x - positionXYStart.x) / 7).toFloat(),
            (positionXYStart.y + (6 * (positionXYEnd.y - positionXYStart.y) / 7)).toFloat(),
            positionXYEnd.x.toFloat(),
            positionXYEnd.y.toFloat()
        )
        canvas.drawPath(path, paint_sen)
    }
}
