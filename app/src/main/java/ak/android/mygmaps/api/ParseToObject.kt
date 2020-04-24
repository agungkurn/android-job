package ak.android.mygmaps.api

import ak.android.mygmaps.model.BlueDots
import com.google.gson.Gson

object ParseToObject {
    private val gson = Gson()

    fun parse(json: String): BlueDots {
        return gson.fromJson(json, BlueDots::class.java)
    }
}