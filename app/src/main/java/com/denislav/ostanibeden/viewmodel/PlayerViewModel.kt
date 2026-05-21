package com.denislav.ostanibeden.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denislav.ostanibeden.data.local.Player
import com.denislav.ostanibeden.data.repository.PlayerRepository
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val repository: PlayerRepository
) : ViewModel() {

    suspend fun getPlayer(): Player? {
        return repository.getPlayer()
    }

    fun insertPlayer(player: Player) {

        viewModelScope.launch {

            repository.insertPlayer(player)
        }
    }

    fun updatePlayer(player: Player) {

        viewModelScope.launch {

            repository.updatePlayer(player)
        }
    }
}