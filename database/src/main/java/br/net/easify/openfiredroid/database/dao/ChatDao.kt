package br.net.easify.openfiredroid.database.dao

import androidx.room.*
import br.net.easify.openfiredroid.database.model.Chat

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat WHERE contact_id = :contact_id ORDER BY chat_id")
    fun getChatFromContact(contact_id: Long): List<Chat>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(chat: Chat): Long

    @Query("DELETE FROM chat WHERE chat_id = :chat_id")
    fun delete(chat_id: Long)

    @Query("DELETE FROM chat")
    fun deleteAll()

    @Query("DELETE FROM chat WHERE contact_id = :contact_id")
    fun deleteChatFromContact(contact_id: Long)
}