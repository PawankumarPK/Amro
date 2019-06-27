package com.example.amro.api.models.StockModels

import com.google.gson.annotations.SerializedName

class StateModel {

    @SerializedName("tripId")
    var tripId : Int? = null

    @SerializedName("tripStatus")
    var tripStatus: Int? = null

}
