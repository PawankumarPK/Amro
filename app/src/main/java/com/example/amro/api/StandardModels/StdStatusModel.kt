package com.example.amro.api.StandardModels

import com.google.gson.annotations.SerializedName

/**
 * Created by ajayvishnu on 05/06/19.
 */

enum class STATUS(val statusVal:Int) {
    UNINITIALIZED(0),
    INITIALIZED(10),
    READY(20),
    DISPATCH_AUTH(30),
    INVENTORY_SCAN_IN(40),
    DOOR_CLOSED(50),
    GOAL_RECEIVED(60),
    MOVING(70),
    GOAL_REACHED(80),
    DELIVERY_AUTH(90),
    INVENTORY_SCAN_OUT(100),
    DELIVERY_COMPLETE(110),
    DELIVERY_CANCELLED(120),
    MOVE_ERROR(130),
    HARDWARE_ERROR(140),
    BATTERY_OUT(150),
    CHARGING(160)
}

class StdStatusModel {
    @SerializedName("status")
    var status: STATUS? = STATUS.UNINITIALIZED
}