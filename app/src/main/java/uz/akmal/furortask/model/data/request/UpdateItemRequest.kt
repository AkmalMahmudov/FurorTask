package uz.akmal.furortask.model.data.request

data class UpdateItemRequest(
    val address: String,
    val cost: Int,
    val created_date: String,
    val id: Int,
    val name_uz: String,
    val product_type_id: Int
)