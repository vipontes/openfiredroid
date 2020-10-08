package br.net.easify.openfiredroid.database.dao

import androidx.room.*
import br.net.easify.openfiredroid.database.model.User


@Dao
interface UserDao {

    @Query("SELECT * FROM user LIMIT 1")
    fun getLoggedUser(): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User): Long

    @Query("DELETE FROM user WHERE user_id = :userId")
    fun delete(userId: Long)

    @Query("DELETE FROM user")
    fun delete()

    @Update
    fun update(user: User?)
}