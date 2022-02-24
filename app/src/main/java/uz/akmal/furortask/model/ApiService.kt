package uz.akmal.furortask.model

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import uz.akmal.furortask.model.data.response.GetItemResponse

interface ApiService {
    @GET("api/product")
    suspend fun getItemsList(): Response<List<GetItemResponse>>

    @POST("api/product")
    suspend fun insertItem(): Response<List<GetItemResponse>>

    @PUT("api/product")
    suspend fun updateItem(): Response<List<GetItemResponse>>

    @DELETE("api/product")
    suspend fun deleteItem(): Response<List<String>>

}