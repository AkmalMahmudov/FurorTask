package uz.akmal.furortask.model.data.response

data class ResItem(
    val address: String,
    val cost: Double,
    val created_date: Long,
    val id: Int,
    val name_uz: String,
    val product_type_id: Int
)