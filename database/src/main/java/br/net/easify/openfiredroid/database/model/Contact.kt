package br.net.easify.openfiredroid.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact")
data class Contact(

    @PrimaryKey(autoGenerate = true)
    var contact_id: Long = 0,
    var contact_name: String,
    var contact_jid: String
)