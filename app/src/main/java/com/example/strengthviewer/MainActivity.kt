package com.example.strengthviewer

import android.graphics.Point
import android.os.Build
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.Sort

class MainActivity : AppCompatActivity() {
    companion object {
        private var xyStrength = Point(10, 120)
        private var xyEpisode = Point(0, 0)
        private const val diff = 200
        private var map: MutableMap<String, NodeEpisode> = mutableMapOf()
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        CommonFunction.getDisplaySize(this)     //Get X axis display size to define start position of Episode
        val frameLayout = FrameLayout(this)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        //Initialize Realm
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            //.deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)

        /*       var realm: Realm = Realm.getDefaultInstance().apply {
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
               }*/
        val constraintLayout: ConstraintLayout = ConstraintLayout(this).apply {
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
            var nStrength: NodeStrength = NodeStrength(this, rcd.idStrength, xyStrength).apply {
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
                if (!map.contains(epd.idEpisode)) {
                    //Create NodeEpisode object
                    var nEpisode: NodeEpisode = NodeEpisode(this, epd.episode, xyEpisode).apply {
                        id = View.generateViewId()
                    }.also {
                        constraintLayout.addView(it)
                        map[epd.idEpisode] = it
                    }

                    val constraintSet: ConstraintSet = ConstraintSet().apply {
                        clone(constraintLayout)
                        constrainHeight(nEpisode.id, CommonFunction.heightNode)
                        constrainWidth(nEpisode.id, ConstraintSet.MATCH_CONSTRAINT_SPREAD)
                    }.also {
                        CommonFunction.setConstraint(
                            it, nStrength, xyEpisode.y - xyStrength.y, nEpisode
                        ).applyTo(constraintLayout)
                    }
                    xyEpisode.offset(0, diff)
                    posY_strength += diff
                }

                //Draw connection between node
                var connection: ConnectionNode =
                    ConnectionNode(
                        this, nStrength.connectPosXY, map[epd.idEpisode]!!.connectPosXY
                    ).also {
                        frameLayout.addView(it)
                    }
            }
        }
        frameLayout.addView(constraintLayout)
        setContentView(frameLayout)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}