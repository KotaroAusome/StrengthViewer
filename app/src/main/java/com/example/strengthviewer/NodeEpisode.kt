package com.example.strengthviewer

import android.content.Context
import android.graphics.Point

class NodeEpisode : NodeBase {
    constructor(context: Context, episode: String, xy: Point) : super(context) {
        init(null, 0)
        text = episode
        positionXY = xy
    }
}