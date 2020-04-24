package ak.android.mygmaps.api

import ak.android.mygmaps.MapsActivity
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class ApiRequest(private val latlong: LatLng) {
    internal fun generateCoordinates(): String {
        var response = """
            {
                "results": [
        """.trimIndent()

        for (i in 0..9) {
            val point = randomPoint()

            response += """
                {
                    "idx": $i,
                    "latitude": ${point.latitude},
                    "longitude": ${point.longitude}
                }
            """.trimIndent()

            if (i != 9) {
                response += """
                   , 
                """.trimIndent()
            } else {
                response += """
                    ]}
                """.trimIndent()
            }
        }

        return response
    }

    private fun randomPoint(): LatLng {
        Log.d(MapsActivity::class.java.simpleName, "generating...")
        val x0 = latlong.latitude
        val y0 = latlong.longitude
        val r = 0.02    // 0.01 = 1 km

        val u = Math.random()
        val v = Math.random()

        val w = r * sqrt(u)
        val t = 2 * PI * v
        val x = w * cos(t)
        val y = w * sin(t)

        return LatLng(x + x0, y + y0)
    }
}