package br.net.easify.openfiredroid.database.dao

import androidx.room.*
import br.net.easify.openfiredroid.database.model.Contact

@Dao
interface ContactDao {

    @Query("SELECT * FROM contact")
    fun getAll(): List<Contact>

    @Query("SELECT * FROM contact WHERE contact_id = :contact_id")
    fun get(contact_id: Long): Contact

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contact: Contact): Long

    @Query("DELETE FROM contact WHERE contact_id = :contact_id")
    fun delete(contact_id: Long)

    @Query("DELETE FROM contact")
    fun deleteAll()

    @Update
    fun update(contact: Contact)
}