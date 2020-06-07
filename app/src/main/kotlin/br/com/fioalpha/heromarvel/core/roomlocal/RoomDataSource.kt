package br.com.fioalpha.heromarvel.core.roomlocal

import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import io.reactivex.Completable
import io.reactivex.Single

@Database(entities = [CharacterFavoriteRoom::class], version = 2,  exportSchema = false)
abstract class AppDatabase: RoomDatabase(){
    abstract fun characterFavoriteDao(): CharacterFavoriteDao
}

@Entity
data class CharacterFavoriteRoom(
    @PrimaryKey
    var id: Long? = null,
    val name: String? = null,
    val pathImage: String? = null,
    val description: String? = null
)

@Dao
interface CharacterFavoriteDao {

    @Query("SELECT * FROM characterfavoriteroom")
    fun getAll(): Single<List<CharacterFavoriteRoom>>

    @Query("SELECT * FROM characterfavoriteroom WHERE id == (:id)")
    fun getById(id: Long): Single<CharacterFavoriteRoom>

    @Insert(onConflict = IGNORE)
    fun save(characterFavoriteRoom: CharacterFavoriteRoom): Completable

    @Delete
    fun delete(characterFavoriteRoom: CharacterFavoriteRoom): Completable

}

