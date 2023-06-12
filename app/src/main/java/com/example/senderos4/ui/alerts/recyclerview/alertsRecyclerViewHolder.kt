import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.senderos4.databinding.ItemAlertBinding
import com.example.senderos4.ui.alerts.model.Alerta

class AlertsRecyclerViewHolder(private val binding: ItemAlertBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(alerta: Alerta){
        binding.aler = alerta.
        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup): AlertsRecyclerViewHolder {
            val binding = ItemAlertBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return AlertsRecyclerViewHolder(binding)
        }
    }
}