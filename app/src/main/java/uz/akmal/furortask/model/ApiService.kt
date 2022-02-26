package uz.akmal.furortask.model


import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import uz.akmal.furortask.model.data.request.InsertItemRequest
import uz.akmal.furortask.model.data.request.UpdateItemRequest
import uz.akmal.furortask.model.data.response.GetItemResponse

interface ApiService {
    @GET("api/product")
    suspend fun getItemsList(): Response<List<GetItemResponse>>

    @GET("api/product")
    suspend fun getItemsAll(@Query("page") page: Int, @Query("perPage") perPage: Int): Response<List<GetItemResponse>>

    @POST("api/product")
    suspend fun insertItem(@Body data: InsertItemRequest): Call<Any>

    @PUT("api/product")
    suspend fun updateItem(@Body data: UpdateItemRequest): Call<Any>

    @DELETE("api/product/{id}")
    fun deleteItem(@Path("id") id: Int): Call<Any>

}