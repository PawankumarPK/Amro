package com.example.amro.api.models.Navigation

import com.google.gson.annotations.SerializedName

class RosMap {

    @SerializedName("resolution")
    var resolution: Float? = 0.0F

    @SerializedName("width")
    var width: Float? = 0.0F

    @SerializedName("height")
    var height: Float? = 0.0F

    @SerializedName("position")
    var position: Point3D? = null

    @SerializedName("data")
    var data: Array<Int>? = arrayOf( -1, -1, 0, -1 )
}