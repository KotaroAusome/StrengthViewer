package com.example.strengthviewer

import android.content.Context
import android.graphics.Point

class NodeStrength : NodeBase {
    constructor(context: Context, idStrength: Int, xy: Point) : super(context) {
        init(null, 0)
        text = CommonFunction.strength[idStrength]?.let { context.getString(it) }
        positionXY = xy
    }
}