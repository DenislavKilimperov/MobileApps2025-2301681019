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

        val adminButton =
            findViewById<Button>(R.id.btnAdminPanel)

        val startGameButton =
            findViewById<Button>(R.id.btnStartGame)

        lifecycleScope.launch {

            var player =
                playerViewModel.getPlayer()

            if (player == null) {

                val newPlayer = Player(
                    totalCoins = 0,
                    totalPoints = 0
                )

                playerViewModel.insertPlayer(newPlayer)

                player = newPlayer
            }

            tvTotalCoins.text =
                "Total Coins: ${player.totalCoins}"
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