package br.net.easify.openfiredroid.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat")
data class Chat (

    @PrimaryKey(autoGenerate = true)
    var chat_id: Long,
    var contact_id: Long,
    var message: String,
    var date: String,
    var owner: Boolean
)