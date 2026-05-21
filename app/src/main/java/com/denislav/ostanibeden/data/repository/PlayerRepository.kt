package com.denislav.ostanibeden.data.repository

import com.denislav.ostanibeden.data.local.Player
import com.denislav.ostanibeden.data.local.PlayerDao

class PlayerRepository(
    private val playerDao: PlayerDao
) {

    suspend fun insertPlayer(player: Player) {
        playerDao.insertPlayer(player)
    }

    suspend fun updatePlayer(player: Player) {
        playerDao.updatePlayer(player)
    }

    suspend fun getPlayer(): Player? {
        return playerDao.getPlayer()
    }
}