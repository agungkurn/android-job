package ak.android.mygmaps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.android.gms.maps.model.LatLng

class MapsViewModel : ViewModel() {
    val blueDots = { currentLocation: LatLng ->
        liveData {
            MapsRepository().generateBlueDots(currentLocation)?.requireNoNulls()?.let {
                emit(it)
            }
        }
    }
}