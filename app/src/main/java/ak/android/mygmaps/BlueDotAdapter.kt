package ak.android.mygmaps

import ak.android.mygmaps.model.BlueDot
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_blue_dot.view.*

class BlueDotAdapter(private val blueDots: List<BlueDot>) :
    RecyclerView.Adapter<BlueDotAdapter.BlueDotViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlueDotViewHolder {
        return BlueDotViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_blue_dot, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BlueDotViewHolder, position: Int) {
        holder.bindItem(blueDots[position])
    }

    override fun getItemCount() = blueDots.size

    inner class BlueDotViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal fun bindItem(blueDot: BlueDot) {
            itemView.tv_dot_name.text = "Dot ${blueDot.idx}"
            itemView.tv_dot_location.text = "Lat: ${blueDot.latitude}. Long: ${blueDot.longitude}"
        }
    }
}
