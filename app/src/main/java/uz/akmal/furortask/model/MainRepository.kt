package uz.akmal.furortask.model


import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

        var event: CurrencyEvent? = null
        api.deleteItem(id).enqueue(object : Callback<Any> {
            override fun onFailure(call: Call<Any>, t: Throwable) {
//                   Toast.makeText(this@MainActivity, "Please try again", Toast.LENGTH_LONG).show()
                Log.d("ttt", "onFailure: $t")
                event = CurrencyEvent.Success(1)
            }

            override fun onResponse(
                call: Call<Any>,
                response: Response<Any>
            ) {
                 if (response.isSuccessful) {
                   event= CurrencyEvent.Success("success")
                    Log.d("ttt", "onResponse: success")
                } else {
                    event=CurrencyEvent.Failure(response.message())
                    Log.d("ttt", "onResponse: else")
                }
            }

        })
//            val response = api.deleteItem(id)
//            val result = response
//            if (response.isSuccessful && result != null) {
//                CurrencyEvent.Success(result)
//
//            } else {
//                CurrencyEvent.Failure(response.message())
//            }
        return event ?: CurrencyEvent.Success(1)
    }
}