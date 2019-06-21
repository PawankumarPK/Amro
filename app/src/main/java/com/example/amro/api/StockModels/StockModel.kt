package com.example.amro.api.StockModels

import com.google.gson.annotations.SerializedName

class StockModel {

    @SerializedName("stockQuantity")
    var stockQuantity : Int? = null

    @SerializedName("tripId")
    var tripId : Int? = null

    @SerializedName("stockName")
    var stockName : String? = null


    @SerializedName("stockId")
    var stockId: Int? = null


}
