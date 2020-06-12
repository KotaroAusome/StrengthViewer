package com.example.strengthviewer

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ModelStrength(
    @PrimaryKey
    open var order: Int = 1,
    open var idStrength: Int = 0,
    open var episodes: RealmList<ModelEpisode>? = null
) : RealmObject() {}

open class ModelEpisode(
    @PrimaryKey
    open var idEpisode: String = "",
    open var episode: String = ""
) : RealmObject() {}