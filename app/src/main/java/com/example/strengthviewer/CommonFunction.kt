package com.example.strengthviewer

import android.app.Activity
import android.graphics.Point
import androidx.constraintlayout.widget.ConstraintSet

class CommonFunction {
    companion object {
        val strength = mapOf(
            1 to R.string.strength_Achiever,
            2 to R.string.strength_Activator,
            3 to R.string.strength_Adaptability,
            4 to R.string.strength_Analytical,
            5 to R.string.strength_Arranger,
            6 to R.string.strength_Belief,
            7 to R.string.strength_Command,
            8 to R.string.strength_Communication,
            9 to R.string.strength_Competition,
            10 to R.string.strength_Connectedness,
            11 to R.string.strength_Consistency,
            12 to R.string.strength_Context,
            13 to R.string.strength_Deliberative,
            14 to R.string.strength_Developer,
            15 to R.string.strength_Discipline,
            16 to R.string.strength_Empathy,
            17 to R.string.strength_Focus,
            18 to R.string.strength_Futuristic,
            19 to R.string.strength_Harmony,
            20 to R.string.strength_Ideation,
            21 to R.string.strength_Inclusiveness,
            22 to R.string.strength_Individualization,
            23 to R.string.strength_Input,
            24 to R.string.strength_Intellection,
            25 to R.string.strength_Learner,
            26 to R.string.strength_Maximizer,
            27 to R.string.strength_Positivity,
            28 to R.string.strength_Relator,
            29 to R.string.strength_Responsibility,
            30 to R.string.strength_Restorative,
            31 to R.string.strength_Selfassurance,
            32 to R.string.strength_Significance,
            33 to R.string.strength_Strategic,
            34 to R.string.strength_Woo
        )

        var positionXStart: Int = 0 //X axis display size to define start position of Episode
        var heightNode: Int = 160

        //For first strength
        fun setConstraint(
            cset: ConstraintSet,
            strength: NodeStrength
        ): ConstraintSet {
            // app:layout_constraintLeft_toLeftOf="parent"
            cset.connect(
                strength.id,
                ConstraintSet.LEFT,
                ConstraintSet.PARENT_ID,
                ConstraintSet.LEFT,
                strength.positionXY.x
            )
            // app:layout_constraintTop_toTopOf="parent"
            cset.connect(
                strength.id,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                strength.positionXY.y
            )
            return cset
        }

        //For episode
        fun setConstraint(
            cset: ConstraintSet,
            strength: NodeStrength,
            positionYStart: Int,
            episode: NodeEpisode
        ): ConstraintSet {
            cset.connect(
                episode.id,
                ConstraintSet.LEFT,
                ConstraintSet.PARENT_ID,
                ConstraintSet.LEFT,
                positionXStart / 2
            )
            cset.connect(
                episode.id,
                ConstraintSet.TOP,
                strength.id,
                ConstraintSet.TOP,
                positionYStart
            )
            cset.connect(
                episode.id,
                ConstraintSet.RIGHT,
                ConstraintSet.PARENT_ID,
                ConstraintSet.RIGHT,
                10
            )
            return cset
        }

        fun getDisplaySize(activity: Activity): Unit {
            val display = activity.windowManager.defaultDisplay
            val point: Point? = Point()
            display.getSize(point)

            if (point?.x != null)
                positionXStart = point.x
        }

    }
}