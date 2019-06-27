package com.example.amro.api.models.StandardModels

import com.google.gson.annotations.SerializedName

/**
 * Created by ajayvishnu on 26/06/19.
 */
class SettingsModel {

    @SerializedName("Server")
    var Server: Int? = 0

    @SerializedName("Core")
    var Core: Int? = 0

    @SerializedName("Lidar")
    var Lidar: Int? = 0

    @SerializedName("Arduino")
    var Arduino: Int? = 0

    @SerializedName("Database")
    var Database: Int? = 0

    @SerializedName("Battery")
    var Battery: Int? = 0
}