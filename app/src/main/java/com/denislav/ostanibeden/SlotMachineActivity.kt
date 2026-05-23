package com.denislav.ostanibeden

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

    private var isSpinning = false

    private val symbols = listOf(
        "⭐",
        "🎭",
        "📉",
        "☎️",
        "💀",
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

        if (isSpinning) return

        isSpinning = true

        btnSpin.isEnabled = false

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

        val handler = Handler(Looper.getMainLooper())

        var spinCount = 0

        val spinRunnable = object : Runnable {

            override fun run() {

                val random1 = symbols.random()
                val random2 = symbols.random()
                val random3 = symbols.random()

                tvReel1.text = random1
                tvReel2.text = random2
                tvReel3.text = random3

                spinCount++

                if (spinCount < 30) {

                    handler.postDelayed(this, 50)

                } else {

                    val final1 = symbols.random()
                    val final2 = symbols.random()
                    val final3 = symbols.random()

                    tvReel1.text = final1
                    tvReel2.text = final2
                    tvReel3.text = final3

                    calculateReward(
                        final1,
                        final2,
                        final3
                    )

                    isSpinning = false

                    btnSpin.isEnabled = true
                }
            }
        }

        handler.post(spinRunnable)
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

    private fun badLuck() {

        val player = currentPlayer ?: return

        val curseType = (1..3).random()

        val updatedPlayer = when (curseType) {

            1 -> {

                tvReward.text =
                    "💀 CURSE: NO 50/50"

                player.copy(
                    disable5050 = true
                )
            }

            2 -> {

                tvReward.text =
                    "💀 CURSE: FAKE AUDIENCE"

                player.copy(
                    fakeAudience = true
                )
            }

            else -> {

                tvReward.text =
                    "💀 CURSE: HALF COINS"

                player.copy(
                    halfCoins = true
                )
            }
        }

        currentPlayer = updatedPlayer

        lifecycleScope.launch {

            playerViewModel.updatePlayer(updatedPlayer)
        }
    }

    private fun noReward() {

        tvReward.text =
            "💸 Better luck next time..."
    }
}