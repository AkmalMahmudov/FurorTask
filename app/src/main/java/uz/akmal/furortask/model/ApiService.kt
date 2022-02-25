package uz.akmal.furortask.model

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import uz.akmal.furortask.model.data.request.GetItemPaging
import uz.akmal.furortask.model.data.response.GetItemResponse

interface ApiService {
    @GET("api/product")
    suspend fun getItemsList(): Response<List<GetItemResponse>>
    @GET("api/product")
    suspend fun getItemsAll(@Query ("page")page:Int, @Query("perPage") perPage:Int): Response<List<GetItemResponse>>

    @POST("api/product")
    suspend fun insertItem(): Response<List<GetItemResponse>>

    @PUT("api/product")
    suspend fun updateItem(): Response<List<GetItemResponse>>


    @HTTP(method = "DELETE", path = "api/product", hasBody = true)
    suspend fun deleteItem(@Body id:Int): Response<List<String>>

}