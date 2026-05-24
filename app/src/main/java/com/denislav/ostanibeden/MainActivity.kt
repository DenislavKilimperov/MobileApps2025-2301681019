package com.denislav.ostanibeden

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.denislav.ostanibeden.data.local.AppDatabase
import com.denislav.ostanibeden.data.local.Player
import com.denislav.ostanibeden.data.repository.PlayerRepository
import com.denislav.ostanibeden.viewmodel.PlayerViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var playerViewModel:
            PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        val database =
            AppDatabase.getDatabase(this)

        val playerRepository =
            PlayerRepository(database.playerDao())

        playerViewModel =
            PlayerViewModel(playerRepository)

        val tvTotalCoins =
            findViewById<TextView>(
                R.id.tvTotalCoins
            )

        val tvTotalPoints =
            findViewById<TextView>(
                R.id.tvTotalPoints
            )

        val tvInventory =
            findViewById<TextView>(
                R.id.tvInventory
            )

        val adminButton =
            findViewById<Button>(
                R.id.btnAdminPanel
            )

        val startGameButton =
            findViewById<Button>(
                R.id.btnStartGame
            )

        val btnSlotMachine =
            findViewById<Button>(
                R.id.btnSlotMachine
            )

        val btnGaming =
            findViewById<Button>(
                R.id.btnGaming
            )

        val btnMovies =
            findViewById<Button>(
                R.id.btnMovies
            )

        val btnHistory =
            findViewById<Button>(
                R.id.btnHistory
            )

        lifecycleScope.launch {

            var currentPlayer =
                playerViewModel.getPlayer()

            if (currentPlayer == null) {

                val newPlayer = Player(

                    totalCoins = 0,

                    totalPoints = 0,

                    extra5050 = 0,

                    extraAudience = 0,

                    extraFriend = 0,

                    disable5050 = false,

                    fakeAudience = false,

                    halfCoins = false,

                    slotPityCounter = 0,

                    unlockedGaming = false,

                    unlockedMovies = false,

                    unlockedHistory = false
                )

                playerViewModel
                    .insertPlayer(newPlayer)

                currentPlayer = newPlayer
            }

            tvTotalCoins.text =
                "Total Coins: " +
                        currentPlayer.totalCoins

            tvTotalPoints.text =
                "Points: " +
                        currentPlayer.totalPoints

            val curseCount =
                listOf(
                    currentPlayer.disable5050,
                    currentPlayer.fakeAudience,
                    currentPlayer.halfCoins
                ).count { it }

            tvInventory.text =
                "🎭 ${currentPlayer.extra5050} | " +
                        "📉 ${currentPlayer.extraAudience} | " +
                        "☎️ ${currentPlayer.extraFriend}\n" +
                        "💀 Curses: $curseCount/3"

            if (currentPlayer.unlockedGaming) {

                btnGaming.text =
                    "🎮 Gaming ✅"
            }

            if (currentPlayer.unlockedMovies) {

                btnMovies.text =
                    "🎬 Movies ✅"
            }

            if (currentPlayer.unlockedHistory) {

                btnHistory.text =
                    "🏛 History ✅"
            }
        }

        adminButton.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    AdminActivity::class.java
                )
            )
        }

        startGameButton.setOnClickListener {

            startQuizCategory("General")
        }

        btnSlotMachine.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    SlotMachineActivity::class.java
                )
            )
        }

        btnGaming.setOnClickListener {

            handleCategoryUnlock("gaming")
        }

        btnMovies.setOnClickListener {

            handleCategoryUnlock("movies")
        }

        btnHistory.setOnClickListener {

            handleCategoryUnlock("history")
        }

        ViewCompat
            .setOnApplyWindowInsetsListener(
                findViewById(R.id.main)
            ) { v, insets ->

                val systemBars =
                    insets.getInsets(
                        WindowInsetsCompat
                            .Type
                            .systemBars()
                    )

                v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom
                )

                insets
            }
    }

    private fun handleCategoryUnlock(
        category: String
    ) {

        lifecycleScope.launch {

            val player =
                playerViewModel.getPlayer()

            if (player == null)
                return@launch

            when (category) {

                "gaming" -> {

                    if (player.unlockedGaming) {

                        startQuizCategory(
                            "Gaming"
                        )

                    } else {

                        unlockCategory(
                            player,
                            50,
                            "Gaming"
                        )
                    }
                }

                "movies" -> {

                    if (player.unlockedMovies) {

                        startQuizCategory(
                            "Movies"
                        )

                    } else {

                        unlockCategory(
                            player,
                            75,
                            "Movies"
                        )
                    }
                }

                "history" -> {

                    if (player.unlockedHistory) {

                        startQuizCategory(
                            "History"
                        )

                    } else {

                        unlockCategory(
                            player,
                            100,
                            "History"
                        )
                    }
                }
            }
        }
    }

    private fun unlockCategory(

        player: Player,

        cost: Int,

        category: String

    ) {

        if (player.totalPoints < cost) {

            Toast.makeText(
                this,
                "Not enough points!",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val updatedPlayer = when (category) {

            "Gaming" -> player.copy(

                totalPoints =
                    player.totalPoints - cost,

                unlockedGaming = true
            )

            "Movies" -> player.copy(

                totalPoints =
                    player.totalPoints - cost,

                unlockedMovies = true
            )

            else -> player.copy(

                totalPoints =
                    player.totalPoints - cost,

                unlockedHistory = true
            )
        }

        lifecycleScope.launch {

            playerViewModel
                .updatePlayer(updatedPlayer)

            runOnUiThread {

                Toast.makeText(
                    this@MainActivity,
                    "$category Unlocked!",
                    Toast.LENGTH_SHORT
                ).show()

                startQuizCategory(category)
            }
        }
    }

    private fun startQuizCategory(
        category: String
    ) {

        val intent =
            Intent(
                this,
                QuizActivity::class.java
            )

        intent.putExtra(
            "category",
            category
        )

        startActivity(intent)
    }
}