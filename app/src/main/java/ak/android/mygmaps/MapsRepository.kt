package ak.android.mygmaps

import ak.android.mygmaps.api.ApiRequest
import ak.android.mygmaps.api.ParseToObject
import ak.android.mygmaps.model.BlueDot
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MapsRepository {
    suspend fun generateBlueDots(latLng: LatLng): List<BlueDot?>? {
        return withContext(Dispatchers.IO) {
            val request = ApiRequest(LatLng(latLng.latitude, latLng.longitude))
            val result = request.generateCoordinates()
            Log.d(MapsActivity::class.java.simpleName, "received:\n$result")
            ParseToObject.parse(result).results
        }
    }
}