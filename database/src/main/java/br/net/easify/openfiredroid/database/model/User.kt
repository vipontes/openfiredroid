package br.net.easify.openfiredroid.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(

    @PrimaryKey(autoGenerate = true)
    var user_id: Long = 0,
    var user_name: String,
    var password: String
)