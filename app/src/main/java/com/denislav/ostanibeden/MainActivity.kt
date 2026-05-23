package com.denislav.ostanibeden

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        val database = AppDatabase.getDatabase(this)

        val playerRepository =
            PlayerRepository(database.playerDao())

        val playerViewModel =
            PlayerViewModel(playerRepository)

        val tvTotalCoins =
            findViewById<TextView>(R.id.tvTotalCoins)

        val tvTotalPoints =
            findViewById<TextView>(R.id.tvTotalPoints)

        val tvInventory =
            findViewById<TextView>(R.id.tvInventory)

        val adminButton =
            findViewById<Button>(R.id.btnAdminPanel)

        val startGameButton =
            findViewById<Button>(R.id.btnStartGame)

        val btnSlotMachine =
            findViewById<Button>(R.id.btnSlotMachine)

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
                    halfCoins = false
                )

                playerViewModel.insertPlayer(newPlayer)

                currentPlayer = newPlayer
            }

            tvTotalCoins.text =
                "Total Coins: ${currentPlayer.totalCoins}"

            tvTotalPoints.text =
                "Points: ${currentPlayer.totalPoints}"

            tvInventory.text =
                "🎭 ${currentPlayer.extra5050} | " +
                        "📉 ${currentPlayer.extraAudience} | " +
                        "☎️ ${currentPlayer.extraFriend}"
        }

        adminButton.setOnClickListener {

            val intent =
                Intent(this, AdminActivity::class.java)

            startActivity(intent)
        }

        startGameButton.setOnClickListener {

            val intent =
                Intent(this, QuizActivity::class.java)

            startActivity(intent)
        }

        btnSlotMachine.setOnClickListener {

            val intent =
                Intent(this, SlotMachineActivity::class.java)

            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v, insets ->

            val systemBars =
                insets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
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
}