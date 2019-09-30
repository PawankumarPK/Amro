package com.example.amro.api.models.FloorRoomModels

import com.google.gson.annotations.SerializedName

class FloorModel {

    @SerializedName("floorRooms")
    var floorRooms: ArrayList<RoomModel>? = null

    @SerializedName("floorName")
    var floorName : String? = null


}
