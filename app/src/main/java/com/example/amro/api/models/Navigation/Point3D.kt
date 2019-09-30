package com.example.amro.api.models.Navigation

import com.google.gson.annotations.SerializedName

class Point3D {

    @SerializedName("x")
    var x: Float? = 0.0F

    @SerializedName("y")
    var y: Float? = 0.0F

    @SerializedName("z")
    var z: Float? = 0.0F

}