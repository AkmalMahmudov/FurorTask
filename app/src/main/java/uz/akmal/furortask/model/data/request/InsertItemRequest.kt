package uz.akmal.furortask.model.data.request

data class InsertItemRequest(
    var address: String,
    var cost: Int,
    val created_date: String,
    var name_uz: String,
    val product_type_id: Int
)