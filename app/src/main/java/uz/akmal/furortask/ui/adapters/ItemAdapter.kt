package uz.akmal.furortask.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.akmal.furortask.databinding.ItemRecyclerBinding
import uz.akmal.furortask.model.data.response.GetItemResponse

class ItemAdapter : ListAdapter<GetItemResponse, ItemAdapter.ViewHolder>(GetItemResponse.ITEM_CALLBACK) {
    private val ls = ArrayList<GetItemResponse>()

    var itemClickListener: ((GetItemResponse) -> Unit)? = null
    fun itemClickListener(block: (GetItemResponse) -> Unit) {
        itemClickListener = block
    }

    inner class ViewHolder(val binding: ItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.item.setOnClickListener {
                itemClickListener?.invoke(getItem(absoluteAdapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            name.text = getItem(position).name_uz
            address.text = getItem(position).address
            id.text = getItem(position).id.toString()
            price.text = " ${getItem(position).cost}$"
            date.text = getItem(position).created_date.toString()
        }
    }


    fun removeItem(data: GetItemResponse) {
        val index = ls.indexOfFirst { it.id == data.id }
        ls.removeAt(index)
        notifyItemRemoved(index)
    }

    fun updateItem(data: GetItemResponse) {
        val index = ls.indexOfFirst { it.id == data.id }
        ls[index] = data
        notifyItemChanged(index)
    }

    fun insertItem(data: GetItemResponse) {
        ls.add(data)
        notifyItemInserted(ls.size - 1)
    }
}