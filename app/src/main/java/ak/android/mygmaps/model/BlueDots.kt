package ak.android.mygmaps.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class BlueDots(

    @field:SerializedName("results")
    val results: List<BlueDot?>? = null
)

@Parcelize
data class BlueDot(

    @field:SerializedName("latitude")
    val latitude: Double? = null,

    @field:SerializedName("idx")
    val idx: Int? = null,

    @field:SerializedName("longitude")
    val longitude: Double? = null
) : Parcelable
