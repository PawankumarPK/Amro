package ai.jetbrain.sar.api.StandardModels

import com.google.gson.annotations.SerializedName

/**
 * Created by ajayvishnu on 05/06/19.
 */

enum class STATUS(val statusVal:Int) {
    UNINITIALIZED(0),
    SUCCESS(1),
    FAIL(-1)
}

class StdStatusModel {
    @SerializedName("status")
    var status: STATUS? = STATUS.UNINITIALIZED
}