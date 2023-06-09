import androidx.recyclerview.widget.RecyclerView
import com.example.app.databinding.ItemAlertaBinding

class AlertsRecyclerViewHolder(private val binding: ItemAlertaBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(alerta: Alerta) {
        binding.alerta = alerta
        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup): AlertsRecyclerViewHolder {
            val binding = ItemAlertaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return AlertsRecyclerViewHolder(binding)
        }
    }
}