package uz.akmal.furortask.model.room

import androidx.room.*
import uz.akmal.furortask.model.data.response.GetItemResponse

@Dao
interface ItemDao {
    @Query("SELECT * FROM items_table ORDER BY id ASC")
    fun getItems(): List<GetItemResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: GetItemResponse)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<GetItemResponse>)

    @Update
    fun update(item: GetItemResponse)

    @Delete
    fun delete(item: GetItemResponse)

    @Query("SELECT * FROM items_table WHERE name_uz LIKE :searchQuery")
    fun search(searchQuery: String): List<GetItemResponse>
}