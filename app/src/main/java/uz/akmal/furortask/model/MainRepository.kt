package uz.akmal.furortask.model

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.akmal.furortask.model.data.request.InsertItemRequest
import uz.akmal.furortask.model.data.request.UpdateItemRequest
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

    fun getDeleteItem(id: Int): CurrencyEvent {
        var event: CurrencyEvent? = null
        api.deleteItem(id).enqueue(object : Callback<Any> {
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.d("ttt", "onFailure: $t")
                event = CurrencyEvent.Success(1)
            }

            override fun onResponse(
                call: Call<Any>,
                response: Response<Any>
            ) {
                if (response.isSuccessful) {
                    event = CurrencyEvent.Success("success")
                    Log.d("ttt", "onResponse: success")
                } else {
                    event = CurrencyEvent.Failure(response.message())
                    Log.d("ttt", "onResponse: else")
                }
            }

        })
        return event ?: CurrencyEvent.Loading
    }

    suspend fun insertItem(data: InsertItemRequest): CurrencyEvent {
        var event: CurrencyEvent? = null
        api.insertItem(data).enqueue(object : Callback<Any> {
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.d("ttt", "onFailure: $t")
                event = CurrencyEvent.Success(1)
            }

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    event = CurrencyEvent.Success("success")
                    Log.d("ttt", "onResponse: success")
                } else {
                    event = CurrencyEvent.Failure(response.message())
                    Log.d("ttt", "onResponse: else")
                }
            }

        })
        return event ?: CurrencyEvent.Success(1)
    }

    suspend fun updateItem(data: UpdateItemRequest): CurrencyEvent {
        var event: CurrencyEvent? = null
        api.updateItem(data).enqueue(object : Callback<Any> {
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.d("ttt", "onFailure: $t")
                event = CurrencyEvent.Success(1)
            }

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    event = CurrencyEvent.Success("success")
                    Log.d("ttt", "onResponse: success")
                } else {
                    event = CurrencyEvent.Failure(response.message())
                    Log.d("ttt", "onResponse: else")
                }
            }

        })
        return event ?: CurrencyEvent.Success(1)
    }

    fun getItemsRoom(): List<GetItemResponse> {
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

    fun deleteAllRoom() {
        dao.deleteAll()
    }
}