package uz.akmal.furortask.model.room

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.akmal.furortask.model.data.response.GetItemResponse

@Database(
    entities = [GetItemResponse::class],
    version = 1,
    exportSchema = false
)
abstract class ItemDatabase: RoomDatabase() {
    abstract fun itemDao(): ItemDao
}