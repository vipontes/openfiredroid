package br.net.easify.openfiredroid.database.dao

import androidx.room.*
import br.net.easify.openfiredroid.database.model.Contact

@Dao
interface ContactDao {

    @Query("SELECT * FROM contact")
    fun getAll(): List<Contact>

    @Query("SELECT * FROM contact WHERE contact_id = :contact_id")
    fun get(contact_id: Long): Contact

    @Query("SELECT * FROM contact WHERE contact_name = :contact_name LIMIT 1")
    fun getContactFromName(contact_name: String): Contact?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contact: Contact): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contacts: List<Contact>)

    @Query("DELETE FROM contact WHERE contact_id = :contact_id")
    fun delete(contact_id: Long)

    @Query("DELETE FROM contact")
    fun deleteAll()

    @Update
    fun update(contact: Contact)
}