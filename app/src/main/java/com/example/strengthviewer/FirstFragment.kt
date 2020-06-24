package com.example.strengthviewer

import android.graphics.Point
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.Sort

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    companion object {
        private var xyStrength = Point(10, 120)
        private var xyEpisode = Point(0, 0)
        private const val diff = 200
        private var mapEpisode: MutableMap<String, NodeEpisode> = mutableMapOf()
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        CommonFunction.getDisplaySize(container?.context as MainActivity)     //Get X axis display size to define start position of Episode
        val view: View = inflater.inflate(R.layout.fragment_first, container, false)
        val frameLayout: FrameLayout = view.findViewById(R.id.flayout_main)

        //Initialize Realm
        Realm.init(container?.context as MainActivity)
        val config = RealmConfiguration.Builder()
            //.deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)

/*        var realm: Realm = Realm.getDefaultInstance().apply {
            executeTransaction { it ->
                val mEpisode1 = it.createObject(ModelEpisode::class.java, "001")
                mEpisode1.episode = "This is test-001."
                val mEpisode2 = it.createObject(ModelEpisode::class.java, "002")
                mEpisode2.episode = "This is test-002."
                val mEpisode3 = it.createObject(ModelEpisode::class.java, "003")
                mEpisode3.episode = "This is test-003."

                val mStrength1 = it.createObject(ModelStrength::class.java, 1)
                mStrength1.idStrength = 18
                mStrength1.episodes?.add(mEpisode1)
                mStrength1.episodes?.add(mEpisode2)
                mStrength1.episodes?.add(mEpisode3)

                val mEpisode4 = it.createObject(ModelEpisode::class.java, "004")
                mEpisode4.episode = "This is test-004."
                val mEpisode5 = it.createObject(ModelEpisode::class.java, "005")
                mEpisode5.episode = "This is test-005."

                val mStrength2 = it.createObject(ModelStrength::class.java, 2)
                mStrength2.idStrength = 3
                mStrength2.episodes?.add(mEpisode1)
                mStrength2.episodes?.add(mEpisode4)
                mStrength2.episodes?.add(mEpisode5)
            }
            close()
        }
*/

        val constraintLayout: ConstraintLayout =
            ConstraintLayout(container?.context).apply {
                layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.MATCH_PARENT
                )
            }

        var realm: Realm = Realm.getDefaultInstance()
        //Get data from Realm.ModelStrength and do foreach
        var posY_strength = 0
        val allRecord =
            realm.where(ModelStrength::class.java).findAll().sort("order", Sort.ASCENDING)
        allRecord.forEach { rcd ->
            xyStrength.offset(0, posY_strength)
            xyEpisode.x = CommonFunction.positionXStart / 2
            xyEpisode.y = xyStrength.y

            //Create NodeStrength object
            var nStrength: NodeStrength =
                NodeStrength(
                    container?.context as MainActivity,
                    rcd.idStrength,
                    xyStrength
                ).apply {
                    id = View.generateViewId()
                }.also {
                    constraintLayout.addView(it)
                }

            val constraintSet: ConstraintSet = ConstraintSet().apply {
                clone(constraintLayout)
                constrainHeight(nStrength.id, CommonFunction.heightNode)
                constrainWidth(nStrength.id, ConstraintSet.WRAP_CONTENT)
            }.also {
                CommonFunction.setConstraint(it, nStrength).applyTo(constraintLayout)
            }

            rcd.episodes?.forEach { epd ->
                if (!mapEpisode.contains(epd.idEpisode)) {
                    //Create NodeEpisode object
                    var nEpisode: NodeEpisode = NodeEpisode(
                        container?.context as MainActivity,
                        epd.episode,
                        xyEpisode
                    ).apply {
                        id = View.generateViewId()
                    }.also {
                        constraintLayout.addView(it)
                        mapEpisode[epd.idEpisode] = it
                    }

                    val constraintSet: ConstraintSet = ConstraintSet().apply {
                        clone(constraintLayout)
                        constrainHeight(nEpisode.id, CommonFunction.heightNode)
                        constrainWidth(nEpisode.id, ConstraintSet.MATCH_CONSTRAINT_SPREAD)
                    }.also {
                        CommonFunction.setConstraint(
                            it,
                            nStrength,
                            xyEpisode.y - xyStrength.y,
                            nEpisode
                        ).applyTo(constraintLayout)
                    }
                    xyEpisode.offset(0, diff)
                    posY_strength += diff
                }

                //Draw connection between node
                var connection: ConnectionNode =
                    ConnectionNode(
                        container?.context as MainActivity,
                        nStrength.connectPosXY,
                        mapEpisode[epd.idEpisode]!!.connectPosXY
                    ).also {
                        frameLayout.addView(it)
                    }
            }
        }
        frameLayout.addView(constraintLayout)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        view.findViewById<Button>(R.id.button_first).setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
    }
}