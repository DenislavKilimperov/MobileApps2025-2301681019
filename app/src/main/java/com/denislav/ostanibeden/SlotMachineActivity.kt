package com.denislav.ostanibeden

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.denislav.ostanibeden.data.local.AppDatabase
import com.denislav.ostanibeden.data.local.Player
import com.denislav.ostanibeden.data.repository.PlayerRepository
import com.denislav.ostanibeden.viewmodel.PlayerViewModel
import kotlinx.coroutines.launch

class SlotMachineActivity : AppCompatActivity() {

    private lateinit var playerViewModel: PlayerViewModel

    private lateinit var tvSlotCoins: TextView
    private lateinit var tvReel1: TextView
    private lateinit var tvReel2: TextView
    private lateinit var tvReel3: TextView
    private lateinit var tvReward: TextView

    private lateinit var btnSpin: Button

    private var currentPlayer: Player? = null

    private val symbols = listOf(
        "⭐",
        "🎭",
        "📉",
        "☎️",
        "💀",
        "💰",
        "❌"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_slot_machine)

        tvSlotCoins = findViewById(R.id.tvSlotCoins)

        tvReel1 = findViewById(R.id.tvReel1)
        tvReel2 = findViewById(R.id.tvReel2)
        tvReel3 = findViewById(R.id.tvReel3)

        tvReward = findViewById(R.id.tvReward)

        btnSpin = findViewById(R.id.btnSpin)

        val database = AppDatabase.getDatabase(this)

        val repository =
            PlayerRepository(database.playerDao())

        playerViewModel =
            PlayerViewModel(repository)

        loadPlayer()

        btnSpin.setOnClickListener {

            spinMachine()
        }
    }

    private fun loadPlayer() {

        lifecycleScope.launch {

            currentPlayer =
                playerViewModel.getPlayer()

            if (currentPlayer != null) {

                tvSlotCoins.text =
                    "Coins: ${currentPlayer!!.totalCoins}"
            }
        }
    }

    private fun spinMachine() {

        val player = currentPlayer ?: return

        if (player.totalCoins < 10) {

            Toast.makeText(
                this,
                "Not enough coins!",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val updatedPlayer = player.copy(
            totalCoins = player.totalCoins - 10
        )

        currentPlayer = updatedPlayer

        lifecycleScope.launch {

            playerViewModel.updatePlayer(updatedPlayer)
        }

        tvSlotCoins.text =
            "Coins: ${updatedPlayer.totalCoins}"

        val reel1 = symbols.random()
        val reel2 = symbols.random()
        val reel3 = symbols.random()

        tvReel1.text = reel1
        tvReel2.text = reel2
        tvReel3.text = reel3

        calculateReward(reel1, reel2, reel3)
    }

    private fun calculateReward(
        reel1: String,
        reel2: String,
        reel3: String
    ) {

        if (reel1 == reel2 && reel2 == reel3) {

            when (reel1) {

                "⭐" -> rewardPoints(20)

                "🎭" -> reward5050()

                "📉" -> rewardAudience()

                "☎️" -> rewardFriend()

                "💰" -> rewardCoins(50)

                "💀" -> badLuck()

                else -> noReward()
            }

        } else {

            noReward()
        }
    }

    private fun rewardPoints(points: Int) {

        val player = currentPlayer ?: return

        val updatedPlayer = player.copy(
            totalPoints = player.totalPoints + points
        )

        currentPlayer = updatedPlayer

        lifecycleScope.launch {

            playerViewModel.updatePlayer(updatedPlayer)
        }

        tvReward.text =
            "YOU WON $points POINTS ⭐"
    }

    private fun reward5050() {

        val player = currentPlayer ?: return

        val updatedPlayer = player.copy(
            extra5050 = player.extra5050 + 1
        )

        currentPlayer = updatedPlayer

        lifecycleScope.launch {

            playerViewModel.updatePlayer(updatedPlayer)
        }

        tvReward.text =
            "EXTRA 50/50 WON 🎭"
    }

    private fun rewardAudience() {

        val player = currentPlayer ?: return

        val updatedPlayer = player.copy(
            extraAudience =
                player.extraAudience + 1
        )

        currentPlayer = updatedPlayer

        lifecycleScope.launch {

            playerViewModel.updatePlayer(updatedPlayer)
        }

        tvReward.text =
            "EXTRA AUDIENCE JOKER WON 📉"
    }

    private fun rewardFriend() {

        val player = currentPlayer ?: return

        val updatedPlayer = player.copy(
            extraFriend = player.extraFriend + 1
        )

        currentPlayer = updatedPlayer

        lifecycleScope.launch {

            playerViewModel.updatePlayer(updatedPlayer)
        }

        tvReward.text =
            "EXTRA FRIEND JOKER WON ☎️"
    }

    private fun rewardCoins(coins: Int) {

        val player = currentPlayer ?: return

        val updatedPlayer = player.copy(
            totalCoins = player.totalCoins + coins
        )

        currentPlayer = updatedPlayer

        lifecycleScope.launch {

            playerViewModel.updatePlayer(updatedPlayer)
        }

        tvSlotCoins.text =
            "Coins: ${updatedPlayer.totalCoins}"

        tvReward.text =
            "JACKPOT! +$coins COINS 💰"
    }

    private fun badLuck() {

        tvReward.text =
            "💀 CURSED SPIN 💀"
    }

    private fun noReward() {

        tvReward.text =
            "No reward..."
    }
}