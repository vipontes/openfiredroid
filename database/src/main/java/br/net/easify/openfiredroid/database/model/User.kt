package br.net.easify.openfiredroid.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(

    @PrimaryKey
    var user_Id: Long?,
    var user_name: String?,
    var user_password: String?
)