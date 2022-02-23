package uz.akmal.furortask.model

import retrofit2.Response
import retrofit2.http.POST
import uz.akmal.furortask.model.data.response.GetItemResponse

interface ApiService {
    @POST("api/product")
    suspend fun getItemsList(): Response<List<GetItemResponse>>

}