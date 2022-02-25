package uz.akmal.furortask.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.akmal.furortask.databinding.ItemRecyclerBinding
import uz.akmal.furortask.model.data.response.GetItemResponse

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    private val ls = ArrayList<GetItemResponse>()

    var itemClickListener: ((String) -> Unit)? = null
    fun itemClickListener(block: (String) -> Unit) {
        itemClickListener = block
    }

    inner class ViewHolder(val binding: ItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                itemClickListener?.invoke(absoluteAdapterPosition.toString())
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
            name.text = ls[position].name_uz
            address.text = ls[position].address
            id.text = ls[position].id.toString()
            price.text = " ${ls[position].cost}$"
            date.text = "0"
        }
    }

    override fun getItemCount() = ls.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(data: List<GetItemResponse>) {
       ls.clear()
        ls.addAll(data)
        notifyDataSetChanged()
    }
    fun currentList():ArrayList<GetItemResponse>{
        return ls
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