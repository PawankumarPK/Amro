package com.example.amro.api.UserModels

import com.google.gson.annotations.SerializedName

/**
 * Created by ajayvishnu on 05/06/19.
 */

class UserModel {

    @SerializedName("Id")
    var Id: Int? = -1

    @SerializedName("Name")
    var Name: String? = ""

    @SerializedName("TripId")
    var TripId: Int? = -1

}