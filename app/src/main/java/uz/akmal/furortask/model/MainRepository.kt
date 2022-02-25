package uz.akmal.furortask.model

import uz.akmal.furortask.model.data.response.GetItemResponse
import uz.akmal.furortask.model.room.ItemDao
import uz.akmal.furortask.util.CurrencyEvent
import javax.inject.Inject

class MainRepository @Inject constructor(private val api: ApiService, private val dao: ItemDao) {
    suspend fun getItemsAll(page: Int, perPage: Int): CurrencyEvent {
        return try {
            val response = api.getItemsAll(page, perPage)
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

    suspend fun getDeleteItem(id: Int): CurrencyEvent {
        return try {
            val response = api.deleteItem(id)
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

    fun getItemsRoom(): List<GetItemResponse>{
        return dao.getItems()
    }

    suspend fun insertRoom(item: GetItemResponse) {
        dao.insert(item)
    }

    fun insertAllRoom(list: List<GetItemResponse>) {
        dao.insertAll(list)
    }

    fun updateRoom(item: GetItemResponse) {
        dao.update(item)
    }

    fun deleteRoom(item: GetItemResponse) {
        dao.delete(item)
    }

}