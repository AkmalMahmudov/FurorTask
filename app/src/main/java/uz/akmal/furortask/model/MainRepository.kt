package uz.akmal.furortask.model

import uz.akmal.furortask.util.CurrencyEvent
import javax.inject.Inject

class MainRepository @Inject constructor(private val api: ApiService) {
    suspend fun getItemsList(): CurrencyEvent {
        return try {
            val response = api.getItemsList()
            val result = response.body()
            if (response.isSuccessful && result != null) {
                CurrencyEvent.Success(result)
            } else {
                CurrencyEvent.Failure(response.message())
            }
        } catch (e: Exception) {
            CurrencyEvent.Failure(e.message ?: "error")
        }
    }

    /*suspend fun getLotItem(lot_id: String): CurrencyEvent {
        val request = LotItemRequest(lot_id = lot_id)
        return try {
            val response = api.getLotItem(request)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                CurrencyEvent.Success(result)
            } else {
                CurrencyEvent.Failure(response.message())
            }
        } catch (e: Exception) {
            CurrencyEvent.Failure(e.message ?: "error")
        }
    }*/
}