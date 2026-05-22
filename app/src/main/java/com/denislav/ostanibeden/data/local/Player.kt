package com.denislav.ostanibeden.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player")

data class Player(

    @PrimaryKey
    val id: Int = 1,

    val totalCoins: Int,

    val totalPoints: Int,

    val extra5050: Int,

    val extraAudience: Int,

    val extraFriend: Int
)