package br.net.easify.openfiredroid.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "chat", indices = [Index(value = ["contact_id", "date"], unique = true)])
data class Chat (

    @PrimaryKey(autoGenerate = true)
    var chat_id: Long,
    var contact_id: Long,
    var message: String,
    var date: String,
    var owner: Boolean
)