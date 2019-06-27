package com.example.amro.api.models.UserModels

import com.google.gson.annotations.SerializedName

/**
 * Created by ajayvishnu on 05/06/19.
 */

class UserModel {

    @SerializedName("userName")
    var userName: String? = ""

    @SerializedName("userId")
    var userId: Int? = -1

}