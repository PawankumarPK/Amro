package com.example.amro.api.models.StockModels

import com.google.gson.annotations.SerializedName

class StockModel {

    @SerializedName("stockQuantity")
    var stockQuantity : Int? = -1

    @SerializedName("tripId")
    var tripId : Int? = -1

    @SerializedName("stockName")
    var stockName : String? = ""


    @SerializedName("stockId")
    var stockId: Int? = -1


}
