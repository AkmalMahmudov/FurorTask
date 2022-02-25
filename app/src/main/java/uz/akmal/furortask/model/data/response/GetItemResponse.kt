package uz.akmal.furortask.model.data.response

import androidx.recyclerview.widget.DiffUtil

data class GetItemResponse(
    val address: String,
    val cost: Double,
    val created_date: Long,
    val id: Int,
    val name_uz: String,
    val product_type_id: Int
)
{
    companion object{
        val ITEM_CALLBACK=object : DiffUtil.ItemCallback<GetItemResponse>(){
            override fun areItemsTheSame(oldItem: GetItemResponse, newItem: GetItemResponse)=oldItem.id==newItem.id

            override fun areContentsTheSame(oldItem: GetItemResponse, newItem: GetItemResponse)=oldItem.name_uz==newItem.name_uz
        }
    }
}